package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.data.local.FavoritoEntity
import com.tecsup.pokemon.data.local.ItemDao
import com.tecsup.pokemon.data.mapper.aEntity
import com.tecsup.pokemon.data.mapper.aModelo
import com.tecsup.pokemon.data.mapper.idDeUrl
import com.tecsup.pokemon.data.remote.ApiService
import com.tecsup.pokemon.domain.model.Ficha
import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

private const val LIMITE = 1025

private val TIPOS = listOf(
    "normal",
    "fire",
    "water",
    "electric",
    "grass",
    "ice",
    "fighting",
    "poison",
    "ground",
    "flying",
    "psychic",
    "bug",
    "rock",
    "ghost",
    "dragon",
    "dark",
    "steel",
    "fairy"
)

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    /*
     * UID de Firebase del usuario actualmente conectado.
     *
     * null = nadie inició sesión.
     */
    private val uidActual =
        MutableStateFlow<String?>(null)

    // =========================================================
    // LISTA COMPLETA
    // =========================================================

    override val items: Flow<List<Item>> =
        combine(
            dao.todos(),
            uidActual.flatMapLatest { uid ->

                if (uid == null) {

                    flowOf(
                        emptyList<Int>()
                    )

                } else {

                    dao.idsFavoritos(uid)
                }
            }
        ) { entidades, favoritosIds ->

            val favoritosSet =
                favoritosIds.toSet()

            entidades.map { entidad ->

                entidad
                    .aModelo()
                    .copy(
                        esFavorito =
                            entidad.id in favoritosSet
                    )
            }
        }

    // =========================================================
    // FAVORITOS DEL USUARIO ACTUAL
    // =========================================================

    override val favoritos: Flow<List<Item>> =
        uidActual.flatMapLatest { uid ->

            if (uid == null) {

                flowOf(
                    emptyList()
                )

            } else {

                dao.favoritos(uid)
                    .map { lista ->

                        lista.map { entidad ->

                            entidad
                                .aModelo()
                                .copy(
                                    esFavorito = true
                                )
                        }
                    }
            }
        }

    // =========================================================
    // CAMBIAR USUARIO
    // =========================================================

    override fun usarUsuario(
        uid: String?
    ) {

        /*
         * Solo cambia el StateFlow si realmente
         * cambió el usuario.
         */
        if (uidActual.value != uid) {
            uidActual.value = uid
        }
    }

    // =========================================================
    // SINCRONIZAR POKÉDEX
    // =========================================================

    override suspend fun refrescar():
            Result<Int> = try {

        val refs =
            api.listar(LIMITE).results

        val tiposPorId =
            cargarTipos()

        val nuevos =
            refs.map { ref ->

                val id =
                    idDeUrl(ref.url)

                /*
                 * esFavorito ya NO se almacena dentro
                 * del Pokémon.
                 *
                 * Los favoritos están en una tabla aparte
                 * asociados al UID.
                 */
                ref.aEntity(
                    tipos =
                        tiposPorId[id]
                            .orEmpty(),
                    esFavorito = false
                )
            }

        dao.guardarTodos(
            nuevos
        )

        Result.success(
            nuevos.size
        )

    } catch (e: IOException) {

        Result.failure(e)

    } catch (e: HttpException) {

        Result.failure(e)
    }

    // =========================================================
    // TIPOS
    // =========================================================

    private suspend fun cargarTipos():
            Map<Int, List<String>> =
        coroutineScope {

            TIPOS
                .map { tipo ->

                    async {

                        tipo to
                                api.porTipo(tipo)
                                    .pokemon
                    }
                }
                .awaitAll()
                .flatMap { (tipo, slots) ->

                    slots.map {

                        Triple(
                            idDeUrl(
                                it.pokemon.url
                            ),
                            it.slot,
                            tipo
                        )
                    }
                }
                .filter {
                    it.first <= LIMITE
                }
                .groupBy(
                    keySelector = {
                        it.first
                    },
                    valueTransform = {
                        it.second to it.third
                    }
                )
                .mapValues { (_, valores) ->

                    valores
                        .sortedBy {
                            it.first
                        }
                        .map {
                            it.second
                        }
                }
        }

    // =========================================================
    // DETALLE SIMPLE
    // =========================================================

    override suspend fun obtenerDetalle(
        id: Int
    ): Result<Item> = try {

        val d =
            api.detalle(id)

        val favorito =
            esFavoritoActual(id)

        Result.success(
            Item(
                id = d.id,
                titulo = d.name,
                subtitulo =
                    "Altura ${d.height / 10.0} m · " +
                            "Peso ${d.weight / 10.0} kg",
                imagenUrl =
                    "https://raw.githubusercontent.com/" +
                            "PokeAPI/sprites/master/sprites/" +
                            "pokemon/other/official-artwork/" +
                            "${d.id}.png",
                esFavorito = favorito,
                tipos =
                    d.types.map {
                        it.type.name
                    }
            )
        )

    } catch (e: IOException) {

        Result.failure(e)

    } catch (e: HttpException) {

        Result.failure(e)
    }

    // =========================================================
    // FICHA COMPLETA
    // =========================================================

    override suspend fun obtenerFicha(
        id: Int
    ): Result<Ficha> = try {

        val d =
            api.detalle(id)

        val favorito =
            esFavoritoActual(id)

        Result.success(
            Ficha(
                id = d.id,
                nombre = d.name,
                alturaM =
                    d.height / 10.0,
                pesoKg =
                    d.weight / 10.0,
                tipos =
                    d.types.map {
                        it.type.name
                    },
                habilidades =
                    d.abilities.map {
                        it.ability.name
                    },
                stats =
                    d.stats.map {
                        it.stat.name to
                                it.baseStat
                    },
                esFavorito =
                    favorito
            )
        )

    } catch (e: IOException) {

        Result.failure(e)

    } catch (e: HttpException) {

        Result.failure(e)
    }

    // =========================================================
    // ALTERNAR FAVORITO
    // =========================================================

    override suspend fun alternarFavorito(
        id: Int
    ) {

        val uid =
            uidActual.value
                ?: return

        val yaEsFavorito =
            dao.esFavorito(
                uid = uid,
                pokemonId = id
            )

        if (yaEsFavorito) {

            dao.quitarFavorito(
                uid = uid,
                pokemonId = id
            )

        } else {

            dao.agregarFavorito(
                FavoritoEntity(
                    uid = uid,
                    pokemonId = id
                )
            )
        }
    }

    // =========================================================
    // CONSULTAR FAVORITO DEL USUARIO ACTUAL
    // =========================================================

    private suspend fun esFavoritoActual(
        id: Int
    ): Boolean {

        val uid =
            uidActual.value
                ?: return false

        return dao.esFavorito(
            uid = uid,
            pokemonId = id
        )
    }

    // =========================================================
    // ADMIN
    // =========================================================

    override suspend fun cantidadGuardada():
            Int {

        return dao.contar()
    }

    override suspend fun cantidadFavoritos():
            Int {

        val uid =
            uidActual.value
                ?: return 0

        return dao.contarFavoritos(
            uid
        )
    }

    // =========================================================
    // LIMPIAR CACHÉ
    // =========================================================

    override suspend fun limpiarCache() {

        /*
         * Tu pantalla Admin dice:
         *
         * "Se borrarán todos los Pokémon guardados,
         * incluidos los favoritos."
         *
         * Por eso limpiamos ambas tablas.
         */
        dao.borrarTodosLosFavoritos()

        dao.borrarTodo()
    }
}
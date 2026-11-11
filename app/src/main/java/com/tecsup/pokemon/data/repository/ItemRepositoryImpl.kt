package com.tecsup.pokemon.data.repository

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
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

private const val LIMITE = 1025  // Pokédex nacional completa

private val TIPOS = listOf(
    "normal", "fire", "water", "electric", "grass", "ice", "fighting", "poison",
    "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon", "dark", "steel", "fairy"
)

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override val items = dao.todos().map { lista -> lista.map { it.aModelo() } }
    override val favoritos = dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = try {
        // El Upsert sobrescribe esFavorito, así que se leen antes y se reaplican
        val favoritosIds = dao.idsFavoritos().toSet()
        val refs = api.listar(LIMITE).results
        val tiposPorId = cargarTipos()
        val nuevos = refs.map { ref ->
            val id = idDeUrl(ref.url)
            ref.aEntity(tiposPorId[id].orEmpty(), esFavorito = id in favoritosIds)
        }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    // Consulta type/{tipo} y arma el mapa id -> tipos (el principal primero)
    private suspend fun cargarTipos(): Map<Int, List<String>> = coroutineScope {
        TIPOS.map { tipo -> async { tipo to api.porTipo(tipo).pokemon } }
            .awaitAll()
            .flatMap { (tipo, slots) ->
                slots.map { Triple(idDeUrl(it.pokemon.url), it.slot, tipo) }
            }
            .filter { it.first <= LIMITE }
            .groupBy({ it.first }, { it.second to it.third })
            .mapValues { (_, v) -> v.sortedBy { it.first }.map { it.second } }
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val d = api.detalle(id)
        Result.success(
            Item(
                id = d.id,
                titulo = d.name,
                subtitulo = "Altura ${d.height / 10.0} m · Peso ${d.weight / 10.0} kg",
                imagenUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${d.id}.png",
                esFavorito = dao.esFavorito(d.id) ?: false,
                tipos = d.types.map { it.type.name }
            )
        )
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun obtenerFicha(id: Int): Result<Ficha> = try {
        val d = api.detalle(id)
        Result.success(
            Ficha(
                id = d.id,
                nombre = d.name,
                alturaM = d.height / 10.0,
                pesoKg = d.weight / 10.0,
                tipos = d.types.map { it.type.name },
                habilidades = d.abilities.map { it.ability.name },
                stats = d.stats.map { it.stat.name to it.baseStat },
                esFavorito = dao.esFavorito(d.id) ?: false
            )
        )
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)
    override suspend fun cantidadGuardada() = dao.contar()
    override suspend fun cantidadFavoritos() = dao.contarFavoritos()
    override suspend fun limpiarCache() = dao.borrarTodo()
}
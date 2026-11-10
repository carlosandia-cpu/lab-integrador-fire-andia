package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.data.mapper.aItem
import com.tecsup.pokemon.data.remote.ApiService
import com.tecsup.pokemon.domain.model.Ficha
import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService
) : ItemRepository {

    private val datos = MutableStateFlow<List<Item>>(emptyList())

    override val items = datos
    override val favoritos = datos.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> = try {
        val favoritosIds = datos.value.filter { it.esFavorito }.map { it.id }.toSet()
        val lista = api.listar().results
            .map { it.aItem() }
            .map { it.copy(esFavorito = it.id in favoritosIds) }
        datos.value = lista
        Result.success(lista.size)
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val d = api.detalle(id)
        Result.success(
            Item(
                id = d.id,
                titulo = d.name,
                subtitulo = "Altura ${d.height / 10.0} m · Peso ${d.weight / 10.0} kg",
                imagenUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${d.id}.png",
                esFavorito = datos.value.firstOrNull { it.id == d.id }?.esFavorito ?: false,
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
                esFavorito = datos.value.firstOrNull { it.id == d.id }?.esFavorito ?: false
            )
        )
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        datos.value = datos.value.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
    }

    override suspend fun cantidadGuardada() = datos.value.size
    override suspend fun cantidadFavoritos() = datos.value.count { it.esFavorito }
    override suspend fun limpiarCache() { datos.value = emptyList() }
}
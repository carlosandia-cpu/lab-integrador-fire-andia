package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.domain.model.Ficha
import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeItemRepository : ItemRepository {

    private val datos = MutableStateFlow(
        listOf(
            Item(4, "charmander", "#4", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png"),
            Item(5, "charmeleon", "#5", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/5.png"),
            Item(6, "charizard", "#6", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png"),
            Item(37, "vulpix", "#37", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/37.png"),
            Item(58, "growlithe", "#58", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/58.png")
        )
    )

    override val items = datos
    override val favoritos = datos.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> = Result.success(datos.value.size)

    override suspend fun obtenerDetalle(id: Int): Result<Item> =
        datos.value.firstOrNull { it.id == id }
            ?.let { Result.success(it) }
            ?: Result.failure(Exception("No encontrado"))

    override suspend fun obtenerFicha(id: Int): Result<Ficha> =
        Result.failure(Exception("No disponible en el repositorio falso"))

    override suspend fun alternarFavorito(id: Int) {
        datos.value = datos.value.map {
            if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
        }
    }

    override suspend fun cantidadGuardada() = datos.value.size
    override suspend fun cantidadFavoritos() = datos.value.count { it.esFavorito }
    override suspend fun limpiarCache() { datos.value = emptyList() }
}
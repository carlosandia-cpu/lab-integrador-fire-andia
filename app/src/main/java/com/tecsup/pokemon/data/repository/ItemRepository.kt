package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow
import com.tecsup.pokemon.domain.model.Ficha

interface ItemRepository {
    val items: Flow<List<Item>>
    val favoritos: Flow<List<Item>>
    suspend fun refrescar(): Result<Int>
    suspend fun obtenerDetalle(id: Int): Result<Item>
    suspend fun alternarFavorito(id: Int)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
    suspend fun obtenerFicha(id: Int): Result<Ficha>
}
package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.domain.model.Ficha
import com.tecsup.pokemon.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    val items: Flow<List<Item>>

    val favoritos: Flow<List<Item>>

    /*
     * Indica qué usuario de Firebase está usando
     * actualmente la aplicación.
     *
     * Los favoritos se consultarán exclusivamente
     * para ese UID.
     */
    fun usarUsuario(
        uid: String?
    ) {
        // Implementación vacía para repositorios falsos
        // usados en las pruebas unitarias.
    }

    suspend fun refrescar(): Result<Int>

    suspend fun obtenerDetalle(
        id: Int
    ): Result<Item>

    suspend fun obtenerFicha(
        id: Int
    ): Result<Ficha>

    suspend fun alternarFavorito(
        id: Int
    )

    suspend fun cantidadGuardada(): Int

    suspend fun cantidadFavoritos(): Int

    suspend fun limpiarCache()
}
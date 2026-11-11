package com.tecsup.pokemon.data.perfil

import kotlinx.coroutines.flow.Flow

const val AVATAR_POR_DEFECTO = "red"

interface PerfilRepository {
    fun avatar(uid: String): Flow<String>
    suspend fun elegirAvatar(uid: String, avatar: String)
}
package com.tecsup.pokemon.data.perfil

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class PerfilRepositoryImpl(context: Context) : PerfilRepository {

    private val prefs = context.applicationContext
        .getSharedPreferences("perfil", Context.MODE_PRIVATE)

    // Cada vez que cambia, los que observan vuelven a leer
    private val cambios = MutableStateFlow(0)

    override fun avatar(uid: String): Flow<String> = cambios.map {
        prefs.getString("avatar_$uid", AVATAR_POR_DEFECTO) ?: AVATAR_POR_DEFECTO
    }

    override suspend fun elegirAvatar(uid: String, avatar: String) {
        prefs.edit().putString("avatar_$uid", avatar).apply()
        cambios.value++
    }
}
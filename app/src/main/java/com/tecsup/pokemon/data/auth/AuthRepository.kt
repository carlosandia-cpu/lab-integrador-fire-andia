package com.tecsup.pokemon.data.auth

import com.tecsup.pokemon.domain.model.Sesion

interface AuthRepository {

    suspend fun iniciarSesion(
        correo: String,
        clave: String
    ): Result<Sesion>

    suspend fun registrarUsuario(
        correo: String,
        clave: String
    ): Result<Unit>

    suspend fun sesionActual(): Sesion?

    fun cerrarSesion()
}
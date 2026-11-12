
package com.tecsup.pokemon.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(
        correo: String,
        clave: String
    ): Result<Sesion> = try {
        val usuario = auth.signInWithEmailAndPassword(
            correo, clave
        ).await().user ?: error("Usuario no encontrado")

        Result.success(leerSesion(usuario.uid, correo))

    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        auth.signOut()
        Result.failure(Exception(mensajeDe(e)))
    }

    override suspend fun registrarUsuario(
        correo: String,
        clave: String
    ): Result<Sesion> = try {

        val usuario = auth.createUserWithEmailAndPassword(
            correo, clave
        ).await().user ?: error("No se creó el usuario")

        val datos = hashMapOf(
            "correo" to correo,
            "rol" to "usuario"
        )

        db.collection("usuarios")
            .document(usuario.uid)
            .set(datos)
            .await()

        Result.success(
            Sesion(usuario.uid, correo, Rol.USUARIO)
        )

    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        auth.signOut()
        Result.failure(Exception(mensajeDe(e)))
    }

    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null

        return try {
            leerSesion(
                usuario.uid,
                usuario.email ?: ""
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    override fun cerrarSesion() {
        auth.signOut()
    }

    private suspend fun leerSesion(
        uid: String,
        correo: String
    ): Sesion {
        val documento = db.collection("usuarios")
            .document(uid)
            .get()
            .await()

        val rol = if (
            documento.getString("rol") == "admin"
        ) Rol.ADMIN else Rol.USUARIO

        return Sesion(uid, correo, rol)
    }

    private fun mensajeDe(e: Exception): String = when (e) {
        is FirebaseAuthUserCollisionException ->
            "Este correo ya está registrado"

        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException ->
            "Correo o contraseña incorrectos"

        is FirebaseNetworkException ->
            "No hay conexión a internet"

        is FirebaseFirestoreException ->
            "No se pudo guardar o leer el usuario"

        else -> "No se pudo completar la operación"
    }
}

package com.tecsup.pokemon.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
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

        val usuario = auth
            .signInWithEmailAndPassword(
                correo,
                clave
            )
            .await()
            .user
            ?: error("Usuario no encontrado")

        val sesion = leerSesion(
            uid = usuario.uid,
            correo = correo
        )

        Result.success(sesion)

    } catch (e: CancellationException) {
        throw e

    } catch (e: Exception) {

        auth.signOut()

        Result.failure(
            Exception(mensajeDe(e))
        )
    }

    override suspend fun registrarUsuario(
        correo: String,
        clave: String
    ): Result<Unit> {

        var usuarioCreado: FirebaseUser? = null

        return try {

            // 1. Crear usuario en Firebase Authentication
            val usuario = auth
                .createUserWithEmailAndPassword(
                    correo,
                    clave
                )
                .await()
                .user
                ?: error("No se pudo crear el usuario")

            usuarioCreado = usuario

            // 2. Datos que se guardarán en Firestore
            val datos = hashMapOf(
                "correo" to correo,
                "rol" to "usuario"
            )

            // 3. Crear documento usuarios/{uid}
            db.collection("usuarios")
                .document(usuario.uid)
                .set(datos)
                .await()

            // 4. IMPORTANTE:
            // Firebase deja iniciada la sesión automáticamente
            // después del registro.
            //
            // Nosotros la cerramos para obligar al usuario
            // a iniciar sesión normalmente.
            auth.signOut()

            Result.success(Unit)

        } catch (e: CancellationException) {
            throw e

        } catch (e: Exception) {

            /*
             * Si Authentication logró crear la cuenta
             * pero Firestore falló, intentamos eliminar
             * esa cuenta para no dejar usuarios incompletos.
             */
            try {
                usuarioCreado
                    ?.delete()
                    ?.await()
            } catch (_: Exception) {
                // No detenemos el manejo del error original
            }

            auth.signOut()

            Result.failure(
                Exception(mensajeDe(e))
            )
        }
    }

    override suspend fun sesionActual(): Sesion? {

        val usuario = auth.currentUser
            ?: return null

        return try {

            leerSesion(
                uid = usuario.uid,
                correo = usuario.email ?: ""
            )

        } catch (e: CancellationException) {
            throw e

        } catch (e: Exception) {

            // Si la sesión guardada está dañada,
            // simplemente volvemos al login.
            auth.signOut()

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

        val documento = db
            .collection("usuarios")
            .document(uid)
            .get()
            .await()

        /*
         * Si por alguna razón existe la cuenta
         * en Authentication pero no tiene documento
         * en Firestore, no dejamos entrar.
         */
        if (!documento.exists()) {
            error(
                "No existe información del usuario en Firestore"
            )
        }

        val rolTexto = documento
            .getString("rol")
            ?.lowercase()
            ?: "usuario"

        val rol = if (rolTexto == "admin") {
            Rol.ADMIN
        } else {
            Rol.USUARIO
        }

        return Sesion(
            uid = uid,
            correo = correo,
            rol = rol
        )
    }

    private fun mensajeDe(
        e: Exception
    ): String {

        return when (e) {

            is FirebaseAuthUserCollisionException ->
                "Este correo ya está registrado"

            is FirebaseAuthInvalidCredentialsException ->
                "Correo o contraseña incorrectos"

            is FirebaseAuthInvalidUserException ->
                "Usuario no encontrado"

            is FirebaseNetworkException ->
                "No hay conexión a internet"

            is FirebaseFirestoreException -> {

                when (e.code) {

                    FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                        "Firestore no permite guardar el usuario"

                    FirebaseFirestoreException.Code.UNAVAILABLE ->
                        "Firestore no está disponible en este momento"

                    else ->
                        "No se pudo guardar o leer el usuario"
                }
            }

            else ->
                e.message
                    ?: "No se pudo completar la operación"
        }
    }
}
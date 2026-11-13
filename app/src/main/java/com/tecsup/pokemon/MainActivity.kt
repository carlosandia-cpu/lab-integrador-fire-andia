package com.tecsup.pokemon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.ViewModelFactory
import com.tecsup.pokemon.ui.login.LoginScreen
import com.tecsup.pokemon.ui.login.LoginViewModel
import com.tecsup.pokemon.ui.login.RegistroScreen
import com.tecsup.pokemon.ui.navigation.AppNav
import com.tecsup.pokemon.ui.theme.PokemonTheme

class MainActivity : ComponentActivity() {

    private val container by lazy {
        AppContainer(applicationContext)
    }

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(container)
    }

    /*
     * Si el usuario niega el permiso,
     * la aplicación sigue funcionando normalmente.
     */
    private val pedirPermiso =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            // No hacemos nada si lo rechaza.
        }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        pedirPermisoNotificaciones()

        setContent {

            PokemonTheme {

                val estado by
                loginViewModel.estado
                    .collectAsStateWithLifecycle()

                val sesion =
                    (estado as? UiState.Exito)
                        ?.datos

                /*
                 * false = Login
                 * true = Registro
                 */
                var mostrarRegistro by rememberSaveable {
                    mutableStateOf(false)
                }

                /*
                 * Cada vez que cambia el usuario,
                 * informamos al repositorio qué UID
                 * debe utilizar para los favoritos.
                 *
                 * Esto permite:
                 *
                 * Admin -> favoritos del Admin
                 * Usuario -> favoritos del Usuario
                 */
                LaunchedEffect(sesion?.uid) {

                    container
                        .itemRepository
                        .usarUsuario(
                            sesion?.uid
                        )
                }

                /*
                 * =====================================================
                 * USUARIO CON SESIÓN
                 * =====================================================
                 */
                if (sesion != null) {

                    /*
                     * Si inició sesión correctamente,
                     * nos aseguramos de salir del modo registro.
                     */
                    LaunchedEffect(sesion.uid) {
                        mostrarRegistro = false
                    }

                    AppNav(
                        container = container,
                        sesion = sesion,
                        onCerrarSesion = {

                            /*
                             * Dejamos de utilizar el UID
                             * antes de cerrar la sesión.
                             */
                            container
                                .itemRepository
                                .usarUsuario(null)

                            loginViewModel
                                .cerrarSesion()

                            mostrarRegistro = false
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                } else {

                    /*
                     * =================================================
                     * SIN SESIÓN
                     * =================================================
                     */

                    if (mostrarRegistro) {

                        /*
                         * =========================
                         * REGISTRO
                         * =========================
                         */
                        RegistroScreen(
                            viewModel = loginViewModel,

                            onVolverLogin = {

                                /*
                                 * Volvemos al Login después de:
                                 *
                                 * - pulsar "Ya tienes cuenta"
                                 * - registrar correctamente
                                 */
                                mostrarRegistro = false

                                loginViewModel
                                    .limpiarEstadoRegistro()

                                loginViewModel
                                    .limpiarError()
                            },

                            modifier =
                                Modifier.fillMaxSize()
                        )

                    } else {

                        /*
                         * =========================
                         * LOGIN
                         * =========================
                         */
                        LoginScreen(
                            viewModel = loginViewModel,

                            onIrRegistro = {

                                /*
                                 * Antes de abrir Registro
                                 * limpiamos cualquier error
                                 * anterior del Login.
                                 */
                                loginViewModel
                                    .limpiarError()

                                loginViewModel
                                    .limpiarEstadoRegistro()

                                mostrarRegistro = true
                            },

                            modifier =
                                Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    /*
     * =========================================================
     * PERMISO DE NOTIFICACIONES
     * =========================================================
     */
    private fun pedirPermisoNotificaciones() {

        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
            &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            pedirPermiso.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }
}
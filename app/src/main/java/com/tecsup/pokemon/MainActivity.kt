
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

    private val pedirPermiso =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        pedirPermisoNotificaciones()

        setContent {
            PokemonTheme {

                val estado by loginViewModel.estado
                    .collectAsStateWithLifecycle()

                val sesion =
                    (estado as? UiState.Exito)?.datos

                var mostrarRegistro by rememberSaveable {
                    mutableStateOf(false)
                }

                if (sesion != null) {

                    AppNav(
                        container = container,
                        sesion = sesion,
                        onCerrarSesion = {
                            loginViewModel.cerrarSesion()
                            mostrarRegistro = false
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                } else if (mostrarRegistro) {

                    RegistroScreen(
                        viewModel = loginViewModel,
                        onVolverLogin = {
                            loginViewModel.limpiarError()
                            mostrarRegistro = false
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                } else {

                    LoginScreen(
                        viewModel = loginViewModel,
                        onIrRegistro = {
                            loginViewModel.limpiarError()
                            mostrarRegistro = true
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    private fun pedirPermisoNotificaciones() {

        if (
            Build.VERSION.SDK_INT >= 33 &&
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

package com.tecsup.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.ViewModelFactory
import com.tecsup.pokemon.ui.login.LoginScreen
import com.tecsup.pokemon.ui.login.LoginViewModel
import com.tecsup.pokemon.ui.navigation.AppNav
import com.tecsup.pokemon.ui.theme.PokemonTheme

class MainActivity : ComponentActivity() {

    private val container by lazy { AppContainer(applicationContext) }
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory(container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTheme {
                val estado by loginViewModel.estado.collectAsStateWithLifecycle()
                val sesion = (estado as? UiState.Exito)?.datos

                if (sesion != null) {
                    AppNav(
                        container = container,
                        sesion = sesion,
                        onCerrarSesion = loginViewModel::cerrarSesion,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LoginScreen(loginViewModel, Modifier.fillMaxSize())
                }
            }
        }
    }
}
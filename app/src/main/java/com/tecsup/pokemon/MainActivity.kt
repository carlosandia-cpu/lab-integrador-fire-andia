package com.tecsup.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.tecsup.pokemon.ui.navigation.AppNav
import com.tecsup.pokemon.ui.theme.PokemonTheme

class MainActivity : ComponentActivity() {

    private val container by lazy { AppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTheme {
                AppNav(container, Modifier.fillMaxSize())
            }
        }
    }
}
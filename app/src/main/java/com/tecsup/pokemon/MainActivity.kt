package com.tecsup.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.tecsup.pokemon.ui.ViewModelFactory
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel
import com.tecsup.pokemon.ui.theme.PokemonTheme

class MainActivity : ComponentActivity() {

    private val container by lazy { AppContainer(applicationContext) }
    private val listaViewModel: ListaViewModel by viewModels { ViewModelFactory(container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    ListaScreen(
                        viewModel = listaViewModel,
                        onItemClick = { },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
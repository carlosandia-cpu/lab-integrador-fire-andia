package com.tecsup.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.tecsup.pokemon.data.repository.FakeItemRepository
import com.tecsup.pokemon.notification.NotificadorFalso
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel
import com.tecsup.pokemon.ui.theme.PokemonTheme
import androidx.compose.foundation.layout.padding

class MainActivity : ComponentActivity() {

    private val viewModel = ListaViewModel(FakeItemRepository(), NotificadorFalso())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    ListaScreen(
                        viewModel = viewModel,
                        onItemClick = { },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
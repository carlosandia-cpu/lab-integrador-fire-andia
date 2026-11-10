package com.tecsup.pokemon.ui.favoritos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ItemFila

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {
        Text(
            "Favoritos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> Text(e.mensaje, modifier = Modifier.padding(16.dp))
            is UiState.Exito -> {
                if (e.datos.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aún no tienes favoritos")
                    }
                } else {
                    LazyColumn {
                        items(e.datos, key = { it.id }) { item ->
                            ItemFila(item, onItemClick, viewModel::alternarFavorito)
                        }
                    }
                }
            }
        }
    }
}
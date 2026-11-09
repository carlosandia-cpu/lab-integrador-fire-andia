package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView

@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {
        aviso?.let {
            Text(
                it,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(e.mensaje, onReintentar = viewModel::refrescar)
            is UiState.Exito -> {
                if (e.datos.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay elementos")
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

@Composable
private fun ItemFila(
    item: Item,
    onClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onClick(item.id) },
        headlineContent = { Text(item.titulo.replaceFirstChar { it.uppercase() }) },
        supportingContent = { Text(item.subtitulo) },
        trailingContent = {
            IconButton(onClick = { onFavorito(item.id) }) {
                Icon(
                    if (item.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Favorito"
                )
            }
        }
    )
    HorizontalDivider()
}
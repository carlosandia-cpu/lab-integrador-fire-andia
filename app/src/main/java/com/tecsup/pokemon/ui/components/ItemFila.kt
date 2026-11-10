package com.tecsup.pokemon.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.Tipos

@Composable
fun ItemFila(
    item: Item,
    onClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit
) {
    val tipos = item.tipos.joinToString(", ") { Tipos.nombre(it) }
    ListItem(
        modifier = Modifier.clickable { onClick(item.id) },
        leadingContent = {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                modifier = Modifier.size(56.dp)
            )
        },
        headlineContent = { Text(item.titulo.replaceFirstChar { it.uppercase() }) },
        supportingContent = {
            Text(if (tipos.isEmpty()) item.subtitulo else "${item.subtitulo} · $tipos")
        },
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
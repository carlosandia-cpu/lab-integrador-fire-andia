package com.tecsup.pokemon.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.Tipos

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)

@Composable
fun ItemFila(
    item: Item,
    onClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit
) {
    val colorPrincipal = item.tipos.firstOrNull()?.let { Tipos.color(it) } ?: Azul

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .clickable { onClick(item.id) },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCEBFF).copy(alpha = 0.83f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.50f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(70.dp),
                shape = RoundedCornerShape(16.dp),
                color = colorPrincipal.copy(alpha = 0.22f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = item.imagenUrl,
                        contentDescription = item.titulo,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(60.dp).padding(3.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    "#${item.id.toString().padStart(4, '0')}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Azul.copy(alpha = 0.75f)
                )
                Text(
                    item.titulo.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Azul,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.tipos.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        item.tipos.take(2).forEach { tipo -> EtiquetaTipo(tipo) }
                    }
                } else {
                    Text(item.subtitulo, style = MaterialTheme.typography.bodySmall, color = Azul)
                }
            }
            IconButton(onClick = { onFavorito(item.id) }) {
                Icon(
                    imageVector = if (item.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (item.esFavorito) Dorado else Azul.copy(alpha = 0.65f),
                    modifier = Modifier.size(27.dp)
                )
            }
        }
    }
}

@Composable
private fun EtiquetaTipo(tipo: String) {
    val color = Tipos.color(tipo)
    Surface(color = color, shape = CircleShape) {
        Text(
            text = Tipos.nombre(tipo),
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (color.luminance() > 0.6f) Azul else Color.White,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

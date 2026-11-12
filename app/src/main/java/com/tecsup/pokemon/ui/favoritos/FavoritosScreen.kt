package com.tecsup.pokemon.ui.favoritos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.FondoPokemon
import com.tecsup.pokemon.ui.components.ItemFila

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)

@Composable
fun FavoritosScreen(viewModel: FavoritosViewModel, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    FondoPokemon(modifier = modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Surface(color = Azul.copy(alpha = 0.76f), shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)) {
                Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Dorado, modifier = Modifier.size(34.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("MIS FAVORITOS", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = Dorado)
                        Text("¡Tu equipo Pokémon especial!", color = Color.White, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            when (val e = estado) {
                is UiState.Cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Cargando() }
                is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Surface(color = Color(0xFFDCEAFF).copy(alpha = 0.8f), shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(20.dp)) {
                        Text(e.mensaje, modifier = Modifier.padding(20.dp), color = Azul)
                    }
                }
                is UiState.Exito -> {
                    if (e.datos.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Surface(color = Color(0xFFDCEAFF).copy(alpha = 0.8f), shape = RoundedCornerShape(22.dp), modifier = Modifier.padding(24.dp)) {
                                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Filled.Star, contentDescription = null, tint = Dorado, modifier = Modifier.size(70.dp))
                                    Spacer(Modifier.height(16.dp))
                                    Text("¡Aún no tienes favoritos!", fontWeight = FontWeight.Bold, color = Azul, textAlign = TextAlign.Center)
                                    Text("Explora la Pokédex y forma tu equipo.", color = Azul, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 12.dp)) {
                            items(e.datos, key = { it.id }) { item ->
                                ItemFila(item, onItemClick, viewModel::alternarFavorito)
                            }
                        }
                    }
                }
            }
        }
    }
}

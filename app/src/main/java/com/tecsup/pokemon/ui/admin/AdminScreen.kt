package com.tecsup.pokemon.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.components.FondoPokemon

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)
private val Cristal = Color(0xFFDCEAFF)

@Composable
fun AdminScreen(viewModel: AdminViewModel, modifier: Modifier = Modifier) {
    val datos by viewModel.datos.collectAsStateWithLifecycle()
    val sincronizando by viewModel.sincronizando.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    var confirmarLimpiar by remember { mutableStateOf(false) }

    FondoPokemon(modifier = modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Surface(color = Azul.copy(alpha = 0.76f), shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Text("CENTRO DE CONTROL", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = Dorado)
                    Text("Gestiona tu mundo Pokémon", color = Color.White)
                }
            }
            Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAdmin("Pokémon guardados", datos.guardados, Modifier.weight(1f))
                    TarjetaAdmin("Favoritos", datos.favoritos, Modifier.weight(1f))
                }
                Button(
                    onClick = viewModel::sincronizar,
                    enabled = !sincronizando,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Azul.copy(alpha = 0.85f))
                ) {
                    if (sincronizando) CircularProgressIndicator(Modifier.size(22.dp), color = Dorado, strokeWidth = 2.dp)
                    else {
                        Icon(Icons.Filled.Refresh, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("SINCRONIZAR POKÉDEX")
                    }
                }
                OutlinedButton(
                    onClick = { confirmarLimpiar = true },
                    enabled = !sincronizando,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Cristal.copy(alpha = 0.76f), contentColor = Azul)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Limpiar caché")
                }
                mensaje?.let { texto ->
                    Surface(color = Cristal.copy(alpha = 0.82f), shape = RoundedCornerShape(12.dp)) {
                        Text(texto, modifier = Modifier.padding(12.dp), color = Azul)
                    }
                }
            }
        }
    }
    if (confirmarLimpiar) {
        AlertDialog(
            onDismissRequest = { confirmarLimpiar = false },
            title = { Text("Limpiar caché") },
            text = { Text("Se borrarán todos los Pokémon guardados, incluidos los favoritos.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarLimpiar = false
                    viewModel.limpiarCache()
                }) { Text("Limpiar") }
            },
            dismissButton = { TextButton(onClick = { confirmarLimpiar = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun TarjetaAdmin(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Cristal.copy(alpha = 0.76f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = Azul)
            Text(titulo, style = MaterialTheme.typography.bodySmall, color = Azul)
        }
    }
}

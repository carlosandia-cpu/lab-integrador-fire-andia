package com.tecsup.pokemon.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    modifier: Modifier = Modifier
) {
    val datos by viewModel.datos.collectAsStateWithLifecycle()
    val sincronizando by viewModel.sincronizando.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    var confirmarLimpiar by remember { mutableStateOf(false) }

    Column(
        modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Panel de administrador", style = MaterialTheme.typography.headlineSmall)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Tarjeta("Pokémon guardados", datos.guardados, Modifier.weight(1f))
            Tarjeta("Favoritos", datos.favoritos, Modifier.weight(1f))
        }

        Button(
            onClick = viewModel::sincronizar,
            enabled = !sincronizando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (sincronizando) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Sincronizar")
            }
        }

        OutlinedButton(
            onClick = { confirmarLimpiar = true },
            enabled = !sincronizando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar caché")
        }

        mensaje?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
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
            dismissButton = {
                TextButton(onClick = { confirmarLimpiar = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun Tarjeta(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor.toString(), style = MaterialTheme.typography.headlineLarge)
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
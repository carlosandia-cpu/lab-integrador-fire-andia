package com.tecsup.pokemon.ui.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Ficha
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val shiny by viewModel.shiny.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    (estado as? UiState.Exito)?.let {
                        IconButton(onClick = viewModel::alternarFavorito) {
                            Icon(
                                if (it.datos.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Favorito"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (val e = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(e.mensaje, onReintentar = viewModel::cargar)
                is UiState.Exito -> Contenido(e.datos, shiny, viewModel::alternarShiny)
            }
        }
    }
}

@Composable
private fun Contenido(f: Ficha, shiny: Boolean, onShiny: () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = if (shiny) f.artworkShinyUrl else f.artworkUrl,
            contentDescription = f.nombre,
            modifier = Modifier.size(220.dp)
        )
        Text(
            "#${f.id.toString().padStart(3, '0')} ${f.nombre.replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.headlineMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Versión variocolor")
            Spacer(Modifier.width(8.dp))
            Switch(checked = shiny, onCheckedChange = { onShiny() })
        }
        Spacer(Modifier.height(8.dp))
        Text("Tipos: " + f.tipos.joinToString(", "))
        Text("Altura: ${f.alturaM} m · Peso: ${f.pesoKg} kg")
        Text("Habilidades: " + f.habilidades.joinToString(", "))
        Spacer(Modifier.height(16.dp))
        Text("Estadísticas", style = MaterialTheme.typography.titleMedium)
        f.stats.forEach { (nombre, valor) ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(nombre, modifier = Modifier.width(120.dp))
                Text(valor.toString(), modifier = Modifier.width(40.dp))
                LinearProgressIndicator(
                    progress = { (valor / 255f).coerceIn(0f, 1f) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
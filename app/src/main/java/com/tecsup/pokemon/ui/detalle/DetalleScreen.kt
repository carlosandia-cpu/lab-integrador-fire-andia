
package com.tecsup.pokemon.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

import com.tecsup.pokemon.domain.model.Ficha
import com.tecsup.pokemon.ui.Tipos
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)
private val Fondo = Color(0xFFF2F6FC)

@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val shiny by viewModel.shiny.collectAsStateWithLifecycle()

    val ficha = (estado as? UiState.Exito)?.datos

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Fondo)
            .statusBarsPadding()
    ) {

        // ENCABEZADO CORREGIDO
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Azul
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // BOTON VOLVER
                IconButton(
                    onClick = onVolver,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }

                Text(
                    text = "POKÉMON",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.ExtraBold,
                    color = Dorado,
                    style = MaterialTheme.typography.titleLarge
                )

                // BOTON FAVORITO
                IconButton(
                    onClick = {
                        if (ficha != null) {
                            viewModel.alternarFavorito()
                        }
                    },
                    enabled = ficha != null,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector =
                            if (ficha?.esFavorito == true)
                                Icons.Filled.Star
                            else
                                Icons.Outlined.StarBorder,
                        contentDescription =
                            if (ficha?.esFavorito == true)
                                "Quitar de favoritos"
                            else
                                "Agregar a favoritos",
                        tint = if (ficha != null)
                            Dorado
                        else
                            Color.LightGray
                    )
                }
            }
        }

        // CONTENIDO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (val e = estado) {
                is UiState.Cargando -> {
                    Cargando()
                }

                is UiState.Error -> {
                    ErrorView(
                        e.mensaje,
                        onReintentar = viewModel::cargar
                    )
                }

                is UiState.Exito -> {
                    Contenido(
                        f = e.datos,
                        shiny = shiny,
                        onShiny = viewModel::alternarShiny
                    )
                }
            }
        }
    }
}

@Composable
private fun Contenido(
    f: Ficha,
    shiny: Boolean,
    onShiny: () -> Unit
) {
    val tipoColor = f.tipos.firstOrNull()
        ?.let { Tipos.color(it) } ?: Azul

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // TARJETA PRINCIPAL
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = tipoColor.copy(alpha = 0.17f)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AsyncImage(
                    model = if (shiny)
                        f.artworkShinyUrl
                    else
                        f.artworkUrl,
                    contentDescription = f.nombre,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(220.dp)
                )

                Text(
                    text = "#${f.id.toString().padStart(3, '0')} " +
                            f.nombre.replaceFirstChar {
                                it.uppercase()
                            },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Azul
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    f.tipos.forEach { tipo ->
                        Surface(
                            color = Tipos.color(tipo),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = Tipos.nombre(tipo),
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 5.dp
                                ),
                                color = if (
                                    Tipos.color(tipo).luminance() > 0.6f
                                ) Color.Black else Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // INFORMACION DEL POKEMON
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Versión variocolor",
                        modifier = Modifier.weight(1f),
                        color = Azul,
                        fontWeight = FontWeight.Bold
                    )

                    Switch(
                        checked = shiny,
                        onCheckedChange = {
                            onShiny()
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Azul,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }

                HorizontalDivider()

                Text(
                    text = "Altura: ${f.alturaM} m"
                )

                Text(
                    text = "Peso: ${f.pesoKg} kg"
                )

                Text(
                    text = "Habilidades: " +
                            f.habilidades.joinToString(", ") {
                                Tipos.habilidad(it)
                            }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ESTADISTICAS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {

                Text(
                    text = "ESTADÍSTICAS",
                    color = Azul,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(14.dp))

                f.stats.forEach { (nombre, valor) ->

                    Text(
                        text = "${Tipos.stat(nombre)}  •  $valor",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Azul
                    )

                    Spacer(Modifier.height(5.dp))

                    LinearProgressIndicator(
                        progress = {
                            (valor / 255f).coerceIn(0f, 1f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(9.dp),
                        color = tipoColor,
                        trackColor = Color(0xFFE5EBF5)
                    )

                    Spacer(Modifier.height(14.dp))
                }
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

package com.tecsup.pokemon.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Ficha
import com.tecsup.pokemon.ui.Tipos
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView
import com.tecsup.pokemon.ui.components.FondoPokemon

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)
private val AzulClaro = Color(0xFFDCEAFF)

@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by
    viewModel.estado.collectAsStateWithLifecycle()

    val shiny by
    viewModel.shiny.collectAsStateWithLifecycle()

    val ficha =
        (estado as? UiState.Exito)?.datos

    FondoPokemon(
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                Surface(
                    color =
                        Azul.copy(alpha = 0.88f),
                    shape = RoundedCornerShape(
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onVolver
                        ) {
                            Icon(
                                imageVector =
                                    Icons.AutoMirrored
                                        .Filled.ArrowBack,
                                contentDescription =
                                    "Volver",
                                tint =
                                    Color.White
                            )
                        }

                        Text(
                            text = "POKÉMON",
                            modifier =
                                Modifier.weight(1f),
                            color = Dorado,
                            fontWeight =
                                FontWeight.ExtraBold,
                            style =
                                MaterialTheme.typography
                                    .titleLarge
                        )

                        IconButton(
                            onClick =
                                viewModel::alternarFavorito,
                            enabled = ficha != null
                        ) {
                            Icon(
                                imageVector =
                                    if (
                                        ficha?.esFavorito
                                        == true
                                    ) {
                                        Icons.Filled.Star
                                    } else {
                                        Icons.Outlined
                                            .StarBorder
                                    },
                                contentDescription =
                                    "Favorito",
                                tint =
                                    if (ficha != null)
                                        Dorado
                                    else
                                        Color.LightGray
                            )
                        }
                    }
                }
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (val e = estado) {

                    is UiState.Cargando -> {
                        Box(
                            modifier =
                                Modifier.fillMaxSize(),
                            contentAlignment =
                                Alignment.Center
                        ) {
                            Cargando()
                        }
                    }

                    is UiState.Error -> {
                        Box(
                            modifier =
                                Modifier.fillMaxSize(),
                            contentAlignment =
                                Alignment.Center
                        ) {
                            ErrorView(
                                mensaje =
                                    e.mensaje,
                                onReintentar =
                                    viewModel::cargar
                            )
                        }
                    }

                    is UiState.Exito -> {
                        ContenidoDetalle(
                            f = e.datos,
                            shiny = shiny,
                            onShiny = {
                                viewModel
                                    .alternarShiny()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContenidoDetalle(
    f: Ficha,
    shiny: Boolean,
    onShiny: () -> Unit
) {
    val tipoColor =
        f.tipos.firstOrNull()
            ?.let {
                Tipos.color(it)
            } ?: Azul

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        val horizontal =
            maxWidth > maxHeight

        if (horizontal) {
            Row(
                modifier =
                    Modifier.fillMaxSize(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight()
                        .verticalScroll(
                            rememberScrollState()
                        ),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    TarjetaPrincipalPokemon(
                        f = f,
                        shiny = shiny,
                        tipoColor = tipoColor,
                        horizontal = true
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.58f)
                        .fillMaxHeight()
                        .verticalScroll(
                            rememberScrollState()
                        ),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    TarjetaInfoPokemon(
                        f = f,
                        shiny = shiny,
                        onShiny = onShiny
                    )

                    TarjetaStatsPokemon(
                        f = f,
                        tipoColor =
                            tipoColor
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )
                }
            }

        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {
                TarjetaPrincipalPokemon(
                    f = f,
                    shiny = shiny,
                    tipoColor = tipoColor,
                    horizontal = false
                )

                TarjetaInfoPokemon(
                    f = f,
                    shiny = shiny,
                    onShiny = onShiny
                )

                TarjetaStatsPokemon(
                    f = f,
                    tipoColor = tipoColor
                )

                Spacer(
                    Modifier.height(8.dp)
                )
            }
        }
    }
}

@Composable
private fun TarjetaPrincipalPokemon(
    f: Ficha,
    shiny: Boolean,
    tipoColor: Color,
    horizontal: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(24.dp),
        color =
            Color(0xFFF6EADF)
                .copy(alpha = 0.90f)
    ) {
        Column(
            modifier =
                Modifier.padding(
                    if (horizontal)
                        10.dp
                    else
                        18.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model =
                    if (shiny)
                        f.artworkShinyUrl
                    else
                        f.artworkUrl,
                contentDescription =
                    f.nombre,
                modifier =
                    Modifier.size(
                        if (horizontal)
                            135.dp
                        else
                            220.dp
                    )
            )

            Text(
                text =
                    "#${f.id.toString().padStart(3, '0')} ${
                        f.nombre.replaceFirstChar {
                            it.uppercase()
                        }
                    }",
                style =
                    if (horizontal)
                        MaterialTheme.typography
                            .titleLarge
                    else
                        MaterialTheme.typography
                            .headlineMedium,
                fontWeight =
                    FontWeight.ExtraBold,
                color = Azul
            )

            Spacer(
                Modifier.height(
                    if (horizontal)
                        5.dp
                    else
                        10.dp
                )
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                f.tipos.forEach { tipo ->

                    Surface(
                        color =
                            Tipos.color(tipo),
                        shape =
                            RoundedCornerShape(50)
                    ) {
                        Text(
                            text =
                                Tipos.nombre(tipo),
                            modifier =
                                Modifier.padding(
                                    horizontal =
                                        12.dp,
                                    vertical =
                                        4.dp
                                ),
                            color =
                                if (
                                    Tipos.color(tipo)
                                        .luminance()
                                    > 0.6f
                                ) {
                                    Color.Black
                                } else {
                                    Color.White
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaInfoPokemon(
    f: Ficha,
    shiny: Boolean,
    onShiny: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    AzulClaro.copy(
                        alpha = 0.84f
                    )
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text =
                        "Versión variocolor",
                    modifier =
                        Modifier.weight(1f),
                    color = Azul,
                    fontWeight =
                        FontWeight.Bold
                )

                Switch(
                    checked = shiny,
                    onCheckedChange = {
                        onShiny()
                    },
                    colors =
                        SwitchDefaults.colors(
                            checkedThumbColor =
                                Color.White,
                            checkedTrackColor =
                                Azul,
                            uncheckedThumbColor =
                                Color.White,
                            uncheckedTrackColor =
                                Color.Gray
                        )
                )
            }

            HorizontalDivider()

            Text(
                text =
                    "Altura: ${f.alturaM} m",
                color = Color.Black
            )

            Text(
                text =
                    "Peso: ${f.pesoKg} kg",
                color = Color.Black
            )

            Text(
                text =
                    "Habilidades: ${
                        f.habilidades
                            .joinToString(", ") {
                                Tipos.habilidad(it)
                            }
                    }",
                color = Color.Black
            )
        }
    }
}

@Composable
private fun TarjetaStatsPokemon(
    f: Ficha,
    tipoColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White.copy(
                        alpha = 0.90f
                    )
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "ESTADÍSTICAS",
                color = Azul,
                fontWeight =
                    FontWeight.ExtraBold,
                style =
                    MaterialTheme.typography
                        .titleLarge
            )

            Spacer(
                Modifier.height(14.dp)
            )

            f.stats.forEach {
                    (nombre, valor) ->

                Text(
                    text =
                        "${Tipos.stat(nombre)}  •  $valor",
                    style =
                        MaterialTheme.typography
                            .bodyMedium,
                    color = Azul
                )

                Spacer(
                    Modifier.height(5.dp)
                )

                LinearProgressIndicator(
                    progress = {
                        (valor / 255f)
                            .coerceIn(
                                0f,
                                1f
                            )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp),
                    color = tipoColor,
                    trackColor =
                        Color(0xFFE5EBF5)
                )

                Spacer(
                    Modifier.height(14.dp)
                )
            }
        }
    }
}
package com.tecsup.pokemon.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.FondoPokemon
import com.tecsup.pokemon.ui.components.ItemFila

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)
private val AzulClaro = Color(0xFFDCEAFF)

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by
    viewModel.estado.collectAsStateWithLifecycle()

    FondoPokemon(
        modifier = modifier.fillMaxSize()
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val horizontal = maxWidth > maxHeight

            if (horizontal) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalArrangement =
                        Arrangement.spacedBy(14.dp)
                ) {
                    CabeceraFavoritos(
                        modifier = Modifier
                            .weight(0.34f)
                            .fillMaxHeight(),
                        horizontal = true
                    )

                    ContenidoFavoritos(
                        estado = estado,
                        onItemClick = onItemClick,
                        onFavorito =
                            viewModel::alternarFavorito,
                        modifier = Modifier
                            .weight(0.66f)
                            .fillMaxHeight()
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CabeceraFavoritos(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontal = false
                    )

                    ContenidoFavoritos(
                        estado = estado,
                        onItemClick = onItemClick,
                        onFavorito =
                            viewModel::alternarFavorito,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CabeceraFavoritos(
    modifier: Modifier = Modifier,
    horizontal: Boolean = false
) {
    Surface(
        modifier = modifier,
        color = Azul.copy(alpha = 0.82f),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = if (horizontal) {
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            } else {
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 14.dp
                    )
            },
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Dorado,
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = "MIS FAVORITOS",
                    style =
                        MaterialTheme.typography
                            .headlineSmall,
                    fontWeight =
                        FontWeight.ExtraBold,
                    color = Dorado
                )

                Text(
                    text =
                        "¡Tu equipo Pokémon especial!",
                    color = Color.White,
                    style =
                        MaterialTheme.typography
                            .bodySmall
                )
            }
        }
    }
}

@Composable
private fun ContenidoFavoritos(
    estado: UiState<List<Item>>,
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (val e = estado) {

        is UiState.Cargando -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment =
                    Alignment.Center
            ) {
                Cargando()
            }
        }

        is UiState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment =
                    Alignment.Center
            ) {
                Surface(
                    color =
                        AzulClaro.copy(alpha = 0.85f),
                    shape =
                        RoundedCornerShape(16.dp),
                    modifier =
                        Modifier.padding(20.dp)
                ) {
                    Text(
                        text = e.mensaje,
                        modifier =
                            Modifier.padding(20.dp),
                        color = Azul
                    )
                }
            }
        }

        is UiState.Exito -> {
            if (e.datos.isEmpty()) {

                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment =
                        Alignment.Center
                ) {
                    Surface(
                        color =
                            AzulClaro.copy(
                                alpha = 0.82f
                            ),
                        shape =
                            RoundedCornerShape(22.dp),
                        modifier =
                            Modifier.padding(24.dp)
                    ) {
                        Column(
                            modifier =
                                Modifier.padding(24.dp),
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Filled.Star,
                                contentDescription = null,
                                tint = Dorado,
                                modifier =
                                    Modifier.size(70.dp)
                            )

                            Spacer(
                                Modifier.size(16.dp)
                            )

                            Text(
                                text =
                                    "¡Aún no tienes favoritos!",
                                fontWeight =
                                    FontWeight.Bold,
                                color = Azul,
                                textAlign =
                                    TextAlign.Center
                            )

                            Text(
                                text =
                                    "Explora la Pokédex y forma tu equipo.",
                                color = Azul,
                                textAlign =
                                    TextAlign.Center
                            )
                        }
                    }
                }

            } else {

                LazyColumn(
                    modifier =
                        modifier.fillMaxSize(),
                    contentPadding =
                        PaddingValues(
                            top = 10.dp,
                            bottom = 12.dp
                        )
                ) {
                    items(
                        items = e.datos,
                        key = { it.id }
                    ) { item ->

                        ItemFila(
                            item = item,
                            onClick =
                                onItemClick,
                            onFavorito =
                                onFavorito
                        )
                    }
                }
            }
        }
    }
}
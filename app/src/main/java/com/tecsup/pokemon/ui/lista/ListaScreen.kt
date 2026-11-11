
package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.Regiones
import com.tecsup.pokemon.ui.Tipos
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView
import com.tecsup.pokemon.ui.components.ItemFila

private val RojoPokemon = Color(0xFFE3350D)
private val AzulPokemon = Color(0xFF2A4D8F)
private val Fondo = Color(0xFFF5F7FB)
private val TextoOscuro = Color(0xFF263348)

@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.visibles.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    val tipo by viewModel.tipo.collectAsStateWithLifecycle()
    val region by viewModel.region.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val config = LocalConfiguration.current
    val horizontal = config.screenWidthDp > config.screenHeightDp

    if (horizontal) {
        Row(
            modifier = modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(0.42f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Filtros(
                    busqueda,
                    viewModel::buscar,
                    region,
                    viewModel::elegirRegion,
                    tipo,
                    viewModel::elegirTipo
                )
            }

            Resultados(
                estado = estado,
                aviso = aviso,
                onItemClick = onItemClick,
                onFavorito = viewModel::alternarFavorito,
                onReintentar = viewModel::refrescar,
                onLimpiarFiltros = viewModel::limpiarFiltros,
                isRefreshing = isRefreshing,
                onRefresh = viewModel::refrescar,
                modifier = Modifier.weight(0.58f)
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .then(Modifier)
        ) {
            Filtros(
                busqueda,
                viewModel::buscar,
                region,
                viewModel::elegirRegion,
                tipo,
                viewModel::elegirTipo
            )

            Resultados(
                estado = estado,
                aviso = aviso,
                onItemClick = onItemClick,
                onFavorito = viewModel::alternarFavorito,
                onReintentar = viewModel::refrescar,
                onLimpiarFiltros = viewModel::limpiarFiltros,
                isRefreshing = isRefreshing,
                onRefresh = viewModel::refrescar,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Filtros(
    busqueda: String,
    onBuscar: (String) -> Unit,
    region: Regiones.Region?,
    onRegion: (Regiones.Region?) -> Unit,
    tipo: String?,
    onTipo: (String?) -> Unit
) {
    Column {
        Surface(
            color = RojoPokemon,
            shape = RoundedCornerShape(
                bottomStart = 22.dp,
                bottomEnd = 22.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 14.dp
                    )
            ) {
                Text(
                    text = "POKÉDEX",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Text(
                    text = "¡Explora el mundo Pokémon!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = busqueda,
                    onValueChange = onBuscar,
                    placeholder = {
                        Text("Buscar Pokémon...")
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (busqueda.isNotEmpty()) {
                            IconButton(
                                onClick = { onBuscar("") }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Borrar búsqueda"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = AzulPokemon,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "REGIÓN",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium,
            color = TextoOscuro,
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            item {
                ChipPokemon(
                    "Todas",
                    region == null,
                    AzulPokemon
                ) {
                    onRegion(null)
                }
            }

            items(Regiones.todas) { r ->
                ChipPokemon(
                    r.nombre,
                    region == r,
                    AzulPokemon
                ) {
                    onRegion(r)
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = "TIPO",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium,
            color = TextoOscuro,
            modifier = Modifier.padding(start = 16.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            item {
                ChipPokemon(
                    "Todos",
                    tipo == null,
                    AzulPokemon
                ) {
                    onTipo(null)
                }
            }

            items(Tipos.todos) { t ->
                ChipPokemon(
                    Tipos.nombre(t),
                    tipo == t,
                    Tipos.color(t)
                ) {
                    onTipo(t)
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ChipPokemon(
    texto: String,
    seleccionado: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = {
            Text(
                texto,
                fontWeight = if (seleccionado)
                    FontWeight.Bold
                else FontWeight.Normal
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            selectedContainerColor = color,
            selectedLabelColor = Color.White,
            labelColor = TextoOscuro
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = seleccionado,
            borderColor = Color(0xFFD8DEE9),
            selectedBorderColor = color
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Resultados(
    estado: UiState<List<Item>>,
    aviso: String?,
    onItemClick: (Int) -> Unit,
    onFavorito: (Int) -> Unit,
    onReintentar: () -> Unit,
    onLimpiarFiltros: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (aviso != null) {
                Surface(
                    color = Color(0xFFFFF0D9),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = aviso,
                        color = Color(0xFF955A00),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            when (val e = estado) {
                is UiState.Cargando -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Cargando()
                    }
                }

                is UiState.Error -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorView(
                            e.mensaje,
                            onReintentar = onReintentar
                        )
                    }
                }

                is UiState.Exito -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pokémon encontrados",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextoOscuro
                        )

                        Surface(
                            color = AzulPokemon,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "${e.datos.size}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 5.dp
                                )
                            )
                        }
                    }

                    if (e.datos.isEmpty()) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyResultView(
                                onLimpiarFiltros
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                bottom = 12.dp
                            )
                        ) {
                            items(
                                e.datos,
                                key = { it.id }
                            ) { item ->
                                ItemFila(
                                    item,
                                    onItemClick,
                                    onFavorito
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyResultView(
    onLimpiarFiltros: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PokeBallEmptyIcon()

        Spacer(Modifier.height(18.dp))

        Text(
            text = "¡No encontramos ningún Pokémon!",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = TextoOscuro
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Prueba con otro nombre, región o tipo",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onLimpiarFiltros,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = RojoPokemon
            )
        ) {
            Text("Limpiar filtros")
        }
    }
}

@Composable
private fun PokeBallEmptyIcon() {
    Box(
        modifier = Modifier.size(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(Color(0xFFF5F5F5))
            drawArc(
                color = RojoPokemon,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true
            )
            drawRect(
                color = Color(0xFF212121),
                topLeft = androidx.compose.ui.geometry.Offset(
                    0f,
                    size.height * 0.45f
                ),
                size = androidx.compose.ui.geometry.Size(
                    size.width,
                    size.height * 0.1f
                )
            )
        }

        Surface(
            modifier = Modifier.size(24.dp),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(
                4.dp,
                Color(0xFF212121)
            )
        ) {}
    }
}

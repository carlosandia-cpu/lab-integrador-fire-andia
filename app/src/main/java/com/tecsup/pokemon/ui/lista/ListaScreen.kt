package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.tecsup.pokemon.ui.components.FondoPokemon

private val RojoPokemon = Color(0xFFFFCB05)
private val AzulPokemon = Color(0xFF173B73)

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

    FondoPokemon(modifier = modifier.fillMaxSize()) {
        if (horizontal) {
            Row(
                modifier = Modifier
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
                modifier = Modifier
                    .fillMaxSize()
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
    var menuRegion by remember { mutableStateOf(false) }
    var menuTipo by remember { mutableStateOf(false) }

    Column {
        Surface(
            color = AzulPokemon.copy(alpha = 0.96f),
            shape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text(
                    "POKÉDEX",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = RojoPokemon
                )
                Text(
                    "¡Explora el mundo Pokémon!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = onBuscar,
                    placeholder = { Text("Buscar Pokémon...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (busqueda.isNotEmpty()) {
                            IconButton(onClick = { onBuscar("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Borrar búsqueda")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.95f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.95f),
                        focusedBorderColor = RojoPokemon,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { menuRegion = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AzulPokemon.copy(alpha = 0.88f),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(region?.nombre ?: "Región: Todas", maxLines = 1, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = menuRegion,
                    onDismissRequest = { menuRegion = false },
                    modifier = Modifier.heightIn(max = 288.dp)
                ) {
                    DropdownMenuItem(text = { Text("Todas las regiones") }, onClick = {
                        onRegion(null)
                        menuRegion = false
                    })
                    Regiones.todas.forEach { r ->
                        DropdownMenuItem(text = { Text(r.nombre) }, onClick = {
                            onRegion(r)
                            menuRegion = false
                        })
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { menuTipo = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AzulPokemon.copy(alpha = 0.88f),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(tipo?.let { Tipos.nombre(it) } ?: "Tipo: Todos", maxLines = 1, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = menuTipo,
                    onDismissRequest = { menuTipo = false },
                    modifier = Modifier.heightIn(max = 288.dp)
                ) {
                    DropdownMenuItem(text = { Text("Todos los tipos") }, onClick = {
                        onTipo(null)
                        menuTipo = false
                    })
                    Tipos.todos.forEach { t ->
                        DropdownMenuItem(text = { Text(Tipos.nombre(t)) }, onClick = {
                            onTipo(t)
                            menuTipo = false
                        })
                    }
                }
            }
        }
    }
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
    var pagina by remember { mutableIntStateOf(0) }
    val tamanoPagina = 6
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
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pokémon encontrados",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
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
                        val totalPaginas = (e.datos.size + tamanoPagina - 1) / tamanoPagina
                        val paginaActual = pagina.coerceIn(0, (totalPaginas - 1).coerceAtLeast(0))
                        val visibles = e.datos.drop(paginaActual * tamanoPagina).take(tamanoPagina)
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                bottom = 12.dp
                            )
                        ) {
                            items(
                                visibles,
                                key = { it.id }
                            ) { item ->
                                ItemFila(
                                    item,
                                    onItemClick,
                                    onFavorito
                                )
                            }
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(onClick = { pagina = (paginaActual - 1).coerceAtLeast(0) }, enabled = paginaActual > 0) {
                                        Text("Anterior")
                                    }
                                    Text("${paginaActual + 1} / $totalPaginas", color = Color.White)
                                    OutlinedButton(onClick = { pagina = paginaActual + 1 }, enabled = paginaActual + 1 < totalPaginas) {
                                        Text("Siguiente")
                                    }
                                }
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
            color = Color.White
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Prueba con otro nombre, región o tipo",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onLimpiarFiltros,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulPokemon
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

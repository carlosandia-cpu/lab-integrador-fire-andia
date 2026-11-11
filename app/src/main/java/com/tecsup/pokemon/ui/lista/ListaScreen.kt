package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.ui.Regiones
import com.tecsup.pokemon.ui.Tipos
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando
import com.tecsup.pokemon.ui.components.ErrorView
import com.tecsup.pokemon.ui.components.ItemFila

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
    val horizontal =
        config.screenWidthDp > config.screenHeightDp

    if (horizontal) {
        Row(modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(0.38f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {
                Filtros(
                    busqueda = busqueda,
                    onBuscar = viewModel::buscar,
                    region = region,
                    onRegion = viewModel::elegirRegion,
                    tipo = tipo,
                    onTipo = viewModel::elegirTipo
                )
            }

            VerticalDivider()

            Resultados(
                estado = estado,
                aviso = aviso,
                onItemClick = onItemClick,
                onFavorito = viewModel::alternarFavorito,
                onReintentar = viewModel::refrescar,
                isRefreshing = isRefreshing,
                onRefresh = viewModel::refrescar,
                modifier = Modifier.weight(0.62f)
            )
        }
    } else {
        Column(modifier.fillMaxSize()) {
            Filtros(
                busqueda = busqueda,
                onBuscar = viewModel::buscar,
                region = region,
                onRegion = viewModel::elegirRegion,
                tipo = tipo,
                onTipo = viewModel::elegirTipo
            )

            Resultados(
                estado = estado,
                aviso = aviso,
                onItemClick = onItemClick,
                onFavorito = viewModel::alternarFavorito,
                onReintentar = viewModel::refrescar,
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
    OutlinedTextField(
        value = busqueda,
        onValueChange = onBuscar,
        label = { Text("Buscar por nombre") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )

    Text(
        "Región",
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(start = 12.dp, top = 4.dp)
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = region == null,
                onClick = { onRegion(null) },
                label = { Text("Todas") }
            )
        }

        items(Regiones.todas) { r ->
            FilterChip(
                selected = region == r,
                onClick = { onRegion(r) },
                label = { Text(r.nombre) }
            )
        }
    }

    Text(
        "Tipo",
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(start = 12.dp, top = 4.dp)
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = tipo == null,
                onClick = { onTipo(null) },
                label = { Text("Todos") }
            )
        }

        items(Tipos.todos) { t ->
            FilterChip(
                selected = tipo == t,
                onClick = { onTipo(t) },
                label = { Text(Tipos.nombre(t)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Tipos.color(t)
                )
            )
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
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullRefreshState,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            if (aviso != null) {
                Text(
                    aviso,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
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
                    Text(
                        "${e.datos.size} Pokémon",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(
                            start = 12.dp,
                            top = 6.dp,
                            bottom = 4.dp
                        )
                    )

                    if (e.datos.isEmpty()) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay resultados")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(e.datos, key = { it.id }) { item ->
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

package com.tecsup.pokemon.ui.lista

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.tecsup.pokemon.ui.components.FondoPokemon
import com.tecsup.pokemon.ui.components.ItemFila

private val AmarilloPokemon = Color(0xFFFFCB05)
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

    FondoPokemon(
        modifier = modifier.fillMaxSize()
    ) {
        if (horizontal) {

            Row(
                modifier = Modifier.fillMaxSize()
            ) {

                // FILTROS EN EL LADO IZQUIERDO
                Column(
                    modifier = Modifier
                        .weight(0.40f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
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

                // RESULTADOS EN EL LADO DERECHO
                Resultados(
                    estado = estado,
                    aviso = aviso,
                    onItemClick = onItemClick,
                    onFavorito = viewModel::alternarFavorito,
                    onReintentar = viewModel::refrescar,
                    onLimpiarFiltros = viewModel::limpiarFiltros,
                    isRefreshing = isRefreshing,
                    onRefresh = viewModel::refrescar,
                    modifier = Modifier.weight(0.60f)
                )
            }

        } else {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

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
    var menuRegion by remember {
        mutableStateOf(false)
    }

    var menuTipo by remember {
        mutableStateOf(false)
    }

    Column {

        // CABECERA
        Surface(
            color = AzulPokemon.copy(alpha = 0.82f),
            shape = RoundedCornerShape(
                bottomStart = 22.dp,
                bottomEnd = 22.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {

                Text(
                    text = "POKÉDEX",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = AmarilloPokemon
                )

                Text(
                    text = "¡Explora el mundo Pokémon!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )

                OutlinedTextField(
                    value = busqueda,
                    onValueChange = onBuscar,
                    placeholder = {
                        Text("Buscar Pokémon...")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (busqueda.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onBuscar("")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Borrar búsqueda"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor =
                            Color.White.copy(alpha = 0.92f),

                        unfocusedContainerColor =
                            Color.White.copy(alpha = 0.92f),

                        focusedBorderColor =
                            AmarilloPokemon,

                        unfocusedBorderColor =
                            Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // FILTROS DESPLEGABLES
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            // REGIÓN
            Box(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedButton(
                    onClick = {
                        menuRegion = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor =
                            AzulPokemon.copy(alpha = 0.78f),

                        contentColor =
                            Color.White
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 10.dp
                    )
                ) {

                    Text(
                        text = region?.nombre
                            ?: "Región: Todas",
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector =
                            Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }

                /*
                 * El máximo de 288 dp equivale aproximadamente
                 * a 6 elementos visibles.
                 *
                 * Esto SOLO limita el tamaño visual del menú.
                 * NO limita la cantidad de Pokémon.
                 */
                DropdownMenu(
                    expanded = menuRegion,
                    onDismissRequest = {
                        menuRegion = false
                    },
                    modifier = Modifier.heightIn(
                        max = 288.dp
                    )
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Todas las regiones")
                        },
                        onClick = {
                            onRegion(null)
                            menuRegion = false
                        }
                    )

                    Regiones.todas.forEach { r ->

                        DropdownMenuItem(
                            text = {
                                Text(r.nombre)
                            },
                            onClick = {
                                onRegion(r)
                                menuRegion = false
                            }
                        )
                    }
                }
            }

            // TIPO
            Box(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedButton(
                    onClick = {
                        menuTipo = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor =
                            AzulPokemon.copy(alpha = 0.78f),

                        contentColor =
                            Color.White
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 10.dp
                    )
                ) {

                    Text(
                        text = tipo?.let {
                            Tipos.nombre(it)
                        } ?: "Tipo: Todos",
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector =
                            Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = menuTipo,
                    onDismissRequest = {
                        menuTipo = false
                    },
                    modifier = Modifier.heightIn(
                        max = 288.dp
                    )
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Todos los tipos")
                        },
                        onClick = {
                            onTipo(null)
                            menuTipo = false
                        }
                    )

                    Tipos.todos.forEach { t ->

                        DropdownMenuItem(
                            text = {
                                Text(
                                    Tipos.nombre(t)
                                )
                            },
                            onClick = {
                                onTipo(t)
                                menuTipo = false
                            }
                        )
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
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            // MENSAJE OFFLINE / AVISO
            if (aviso != null) {

                Surface(
                    color = Color(0xFFFFF0D9)
                        .copy(alpha = 0.92f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 4.dp
                        )
                ) {
                    Text(
                        text = aviso,
                        color = Color(0xFF955A00),
                        style =
                            MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            when (val e = estado) {

                // CARGANDO
                is UiState.Cargando -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Cargando()
                    }
                }

                // ERROR
                is UiState.Error -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorView(
                            mensaje = e.mensaje,
                            onReintentar = onReintentar
                        )
                    }
                }

                // ÉXITO
                is UiState.Exito -> {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically,
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Pokémon encontrados",
                            style =
                                MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Surface(
                            color = AzulPokemon.copy(
                                alpha = 0.90f
                            ),
                            shape = CircleShape
                        ) {
                            Text(
                                text = "${e.datos.size}",
                                color = Color.White,
                                style =
                                    MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 5.dp
                                )
                            )
                        }
                    }

                    if (e.datos.isEmpty()) {

                        // ESTADO VACÍO
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyResultView(
                                onLimpiarFiltros =
                                    onLimpiarFiltros
                            )
                        }

                    } else {

                        /*
                         * LISTA COMPLETA DE POKÉMON.
                         *
                         * NO hay límite de 6.
                         * NO hay paginación.
                         *
                         * Se muestran todos los elementos
                         * recibidos desde e.datos.
                         */
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                bottom = 16.dp
                            )
                        ) {

                            items(
                                items = e.datos,
                                key = { item ->
                                    item.id
                                }
                            ) { item ->

                                ItemFila(
                                    item = item,
                                    onClick = onItemClick,
                                    onFavorito = onFavorito
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
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {

        PokeBallEmptyIcon()

        Spacer(
            Modifier.height(18.dp)
        )

        Text(
            text = "¡No encontramos ningún Pokémon!",
            style =
                MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Spacer(
            Modifier.height(6.dp)
        )

        Text(
            text = "Prueba con otro nombre, región o tipo",
            style =
                MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Spacer(
            Modifier.height(20.dp)
        )

        Button(
            onClick = onLimpiarFiltros,
            shape = CircleShape,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        AzulPokemon
                )
        ) {
            Text(
                text = "Limpiar filtros"
            )
        }
    }
}

@Composable
private fun PokeBallEmptyIcon() {

    Box(
        modifier = Modifier.size(72.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            // PARTE BLANCA
            drawCircle(
                color = Color(0xFFF5F5F5)
            )

            // PARTE SUPERIOR
            drawArc(
                color = AmarilloPokemon,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true
            )

            // LÍNEA CENTRAL
            drawRect(
                color = Color(0xFF212121),
                topLeft =
                    androidx.compose.ui.geometry.Offset(
                        x = 0f,
                        y = size.height * 0.45f
                    ),
                size =
                    androidx.compose.ui.geometry.Size(
                        width = size.width,
                        height = size.height * 0.10f
                    )
            )
        }

        // BOTÓN CENTRAL
        Surface(
            modifier = Modifier.size(24.dp),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(
                width = 4.dp,
                color = Color(0xFF212121)
            )
        ) {}
    }
}
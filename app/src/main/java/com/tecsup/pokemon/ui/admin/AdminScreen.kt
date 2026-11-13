package com.tecsup.pokemon.ui.admin

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.ui.components.FondoPokemon

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)
private val AzulClaro = Color(0xFFDCEAFF)

@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    modifier: Modifier = Modifier
) {
    val datos by
    viewModel.datos.collectAsStateWithLifecycle()

    val sincronizando by
    viewModel.sincronizando
        .collectAsStateWithLifecycle()

    val mensaje by
    viewModel.mensaje.collectAsStateWithLifecycle()

    var confirmarLimpiar by remember {
        mutableStateOf(false)
    }

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
                    CabeceraAdmin(
                        modifier = Modifier
                            .weight(0.38f)
                            .fillMaxHeight(),
                        horizontal = true
                    )

                    ContenidoAdmin(
                        guardados =
                            datos.guardados,
                        favoritos =
                            datos.favoritos,
                        sincronizando =
                            sincronizando,
                        mensaje = mensaje,
                        onSincronizar =
                            viewModel::sincronizar,
                        onLimpiar = {
                            confirmarLimpiar = true
                        },
                        modifier = Modifier
                            .weight(0.62f)
                            .fillMaxHeight()
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CabeceraAdmin(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontal = false
                    )

                    ContenidoAdmin(
                        guardados =
                            datos.guardados,
                        favoritos =
                            datos.favoritos,
                        sincronizando =
                            sincronizando,
                        mensaje = mensaje,
                        onSincronizar =
                            viewModel::sincronizar,
                        onLimpiar = {
                            confirmarLimpiar = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    }

    if (confirmarLimpiar) {
        AlertDialog(
            onDismissRequest = {
                confirmarLimpiar = false
            },
            title = {
                Text("Limpiar caché")
            },
            text = {
                Text(
                    "Se borrarán todos los Pokémon guardados, incluidos los favoritos."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmarLimpiar = false
                        viewModel.limpiarCache()
                    }
                ) {
                    Text("Limpiar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        confirmarLimpiar = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun CabeceraAdmin(
    modifier: Modifier = Modifier,
    horizontal: Boolean = false
) {
    Surface(
        modifier = modifier,
        color = Azul.copy(alpha = 0.82f),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = if (horizontal) {
                Modifier
                    .fillMaxSize()
                    .padding(22.dp)
            } else {
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        horizontal = 22.dp,
                        vertical = 16.dp
                    )
            },
            verticalArrangement =
                Arrangement.Center
        ) {
            Text(
                text =
                    "CENTRO DE CONTROL",
                style =
                    MaterialTheme.typography
                        .headlineSmall,
                fontWeight =
                    FontWeight.ExtraBold,
                color = Dorado
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text =
                    "Gestiona tu mundo Pokémon",
                color = Color.White
            )
        }
    }
}

@Composable
private fun ContenidoAdmin(
    guardados: Int,
    favoritos: Int,
    sincronizando: Boolean,
    mensaje: String?,
    onSincronizar: () -> Unit,
    onLimpiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(
                rememberScrollState()
            )
            .padding(4.dp),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {
            TarjetaAdmin(
                titulo =
                    "Pokémon guardados",
                valor = guardados,
                modifier =
                    Modifier.weight(1f)
            )

            TarjetaAdmin(
                titulo = "Favoritos",
                valor = favoritos,
                modifier =
                    Modifier.weight(1f)
            )
        }

        Button(
            onClick = onSincronizar,
            enabled = !sincronizando,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape =
                RoundedCornerShape(14.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        Azul.copy(
                            alpha = 0.90f
                        )
                )
        ) {
            if (sincronizando) {
                CircularProgressIndicator(
                    modifier =
                        Modifier.size(22.dp),
                    color = Dorado,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector =
                        Icons.Filled.Refresh,
                    contentDescription = null
                )

                Spacer(Modifier.size(8.dp))

                Text(
                    "SINCRONIZAR POKÉDEX"
                )
            }
        }

        OutlinedButton(
            onClick = onLimpiar,
            enabled = !sincronizando,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape =
                RoundedCornerShape(14.dp),
            colors =
                ButtonDefaults
                    .outlinedButtonColors(
                        containerColor =
                            AzulClaro.copy(
                                alpha = 0.82f
                            ),
                        contentColor = Azul
                    )
        ) {
            Icon(
                imageVector =
                    Icons.Filled.Delete,
                contentDescription = null
            )

            Spacer(Modifier.size(8.dp))

            Text("Limpiar caché")
        }

        mensaje?.let {
            Surface(
                modifier =
                    Modifier.fillMaxWidth(),
                color =
                    AzulClaro.copy(
                        alpha = 0.85f
                    ),
                shape =
                    RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = it,
                    modifier =
                        Modifier.padding(14.dp),
                    color = Azul,
                    fontWeight =
                        FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun TarjetaAdmin(
    titulo: String,
    valor: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    AzulClaro.copy(
                        alpha = 0.78f
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
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Text(
                text = valor.toString(),
                style =
                    MaterialTheme.typography
                        .headlineLarge,
                fontWeight =
                    FontWeight.ExtraBold,
                color = Azul
            )

            Text(
                text = titulo,
                style =
                    MaterialTheme.typography
                        .bodySmall,
                color = Azul
            )
        }
    }
}
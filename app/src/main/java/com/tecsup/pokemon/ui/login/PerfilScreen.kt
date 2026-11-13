package com.tecsup.pokemon.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.Avatares
import com.tecsup.pokemon.ui.components.FondoPokemon

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)
private val AzulClaro = Color(0xFFDCEAFF)

@Composable
fun PerfilScreen(
    sesion: Sesion,
    viewModel: PerfilViewModel,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val avatar by viewModel.avatar.collectAsStateWithLifecycle()

    var eligiendo by remember {
        mutableStateOf(false)
    }

    FondoPokemon(
        modifier = modifier.fillMaxSize()
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            val horizontal = maxWidth > maxHeight

            if (horizontal) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TarjetaEntrenador(
                        avatar = avatar,
                        onCambiar = {
                            eligiendo = true
                        },
                        modifier = Modifier
                            .weight(0.42f)
                            .fillMaxHeight()
                    )

                    InformacionCuenta(
                        sesion = sesion,
                        onCerrarSesion = onCerrarSesion,
                        modifier = Modifier
                            .weight(0.58f)
                            .fillMaxHeight()
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    TarjetaEntrenador(
                        avatar = avatar,
                        onCambiar = {
                            eligiendo = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    InformacionCuenta(
                        sesion = sesion,
                        onCerrarSesion = onCerrarSesion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }
            }
        }
    }

    if (eligiendo) {
        AlertDialog(
            onDismissRequest = {
                eligiendo = false
            },
            title = {
                Text("Elige tu entrenador")
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(320.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Avatares.todos) { id ->
                        val forma = RoundedCornerShape(12.dp)

                        AsyncImage(
                            model = Avatares.url(id),
                            contentDescription = id,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(forma)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant
                                )
                                .then(
                                    if (id == avatar) {
                                        Modifier.border(
                                            3.dp,
                                            Dorado,
                                            forma
                                        )
                                    } else {
                                        Modifier
                                    }
                                )
                                .clickable {
                                    viewModel.elegirAvatar(id)
                                    eligiendo = false
                                }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        eligiendo = false
                    }
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
private fun TarjetaEntrenador(
    avatar: String,
    onCambiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Surface(
        modifier = modifier,
        color = Azul.copy(alpha = 0.82f),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TARJETA DE ENTRENADOR",
                color = Dorado,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(22.dp))

            AsyncImage(
                model = Avatares.url(avatar),
                contentDescription = "Avatar del entrenador",
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(
                        width = 4.dp,
                        color = Dorado,
                        shape = CircleShape
                    )
                    .clickable {
                        onCambiar()
                    }
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Cambiar entrenador",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable {
                    onCambiar()
                }
            )
        }
    }
}

@Composable
private fun InformacionCuenta(
    sesion: Sesion,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AzulClaro.copy(alpha = 0.82f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "MI CUENTA",
                color = Azul,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            HorizontalDivider()

            Text(
                text = "Correo electrónico",
                color = Azul
            )

            Text(
                text = sesion.correo,
                color = Azul,
                fontWeight = FontWeight.SemiBold
            )

            HorizontalDivider()

            Text(
                text = "Rango de entrenador",
                color = Azul
            )

            Text(
                text = if (sesion.rol == Rol.ADMIN) {
                    "Administrador"
                } else {
                    "Entrenador Pokémon"
                },
                color = Azul,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Azul.copy(alpha = 0.92f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("CERRAR SESIÓN")
            }
        }
    }
}
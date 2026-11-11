
package com.tecsup.pokemon.ui.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.R
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando

private val PokemonRojo = Color(0xFFE3350D)
private val PokemonAzul = Color(0xFF254B91)
private val TextoOscuro = Color(0xFF24344F)

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    var correoError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var claveError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val cargando = estado is UiState.Cargando

    val realizarLogin = {
        if (!cargando) {
            var valido = true
            val emailTrim = correo.trim()

            if (emailTrim.isEmpty()) {
                correoError = "Ingresa tu correo electrónico"
                valido = false
            } else if (
                !Patterns.EMAIL_ADDRESS.matcher(emailTrim).matches()
            ) {
                correoError = "Ingresa un correo válido"
                valido = false
            } else {
                correoError = null
            }

            if (clave.isEmpty()) {
                claveError = "Ingresa tu contraseña"
                valido = false
            } else {
                claveError = null
            }

            if (valido) {
                viewModel.iniciarSesion(emailTrim, clave)
            }
        }
        Unit
    }

    if (cargando && correo.isEmpty() && clave.isEmpty()) {
        Cargando(modifier)
    } else {

        Box(
            modifier = modifier.fillMaxSize()
        ) {

            // FONDO POKÉMON
            Image(
                painter = painterResource(
                    id = R.drawable.fondo_login
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // OSCURECIMIENTO SUAVE DEL FONDO
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.15f)
                    )
            )

            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {

                val pantallaCompacta = maxHeight < 600.dp

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 22.dp)
                        .padding(
                            top = if (pantallaCompacta) 16.dp else 36.dp,
                            bottom = 24.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    // LOGO POKÉDEX
                    Image(
                        painter = painterResource(
                            id = R.drawable.logo_pokedex
                        ),
                        contentDescription = "Pokédex",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .height(
                                if (pantallaCompacta) 95.dp
                                else 135.dp
                            )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "¡Bienvenido, entrenador!",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Tu aventura Pokémon comienza aquí",
                        color = Color.White,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    // TARJETA SEMITRANSPARENTE
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Color.White.copy(alpha = 0.76f)
                            )
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "INICIAR SESIÓN",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PokemonAzul
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Ingresa a tu cuenta de entrenador",
                            fontSize = 12.sp,
                            color = TextoOscuro
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // CAMPO CORREO
                        OutlinedTextField(
                            value = correo,
                            onValueChange = {
                                correo = it
                                correoError = null
                            },
                            label = {
                                Text("Correo electrónico")
                            },
                            singleLine = true,
                            enabled = !cargando,
                            isError = correoError != null,
                            supportingText = {
                                correoError?.let {
                                    Text(it)
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PokemonAzul,
                                focusedLabelColor = PokemonAzul,
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = TextoOscuro,
                                unfocusedTextColor = TextoOscuro,
                                focusedContainerColor =
                                    Color.White.copy(alpha = 0.82f),
                                unfocusedContainerColor =
                                    Color.White.copy(alpha = 0.72f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // CAMPO CONTRASEÑA
                        OutlinedTextField(
                            value = clave,
                            onValueChange = {
                                clave = it
                                claveError = null
                            },
                            label = {
                                Text("Contraseña")
                            },
                            singleLine = true,
                            enabled = !cargando,
                            isError = claveError != null,
                            supportingText = {
                                claveError?.let {
                                    Text(it)
                                }
                            },
                            visualTransformation =
                                if (passwordVisible)
                                    VisualTransformation.None
                                else
                                    PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { realizarLogin() }
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        passwordVisible = !passwordVisible
                                    }
                                ) {
                                    Icon(
                                        imageVector =
                                            if (passwordVisible)
                                                Icons.Filled.VisibilityOff
                                            else
                                                Icons.Filled.Visibility,
                                        contentDescription =
                                            if (passwordVisible)
                                                "Ocultar contraseña"
                                            else
                                                "Mostrar contraseña",
                                        tint = PokemonAzul
                                    )
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PokemonAzul,
                                focusedLabelColor = PokemonAzul,
                                unfocusedBorderColor = Color.Gray,
                                focusedTextColor = TextoOscuro,
                                unfocusedTextColor = TextoOscuro,
                                focusedContainerColor =
                                    Color.White.copy(alpha = 0.82f),
                                unfocusedContainerColor =
                                    Color.White.copy(alpha = 0.72f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // ERROR DE FIREBASE
                        (estado as? UiState.Error)?.let {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = it.mensaje,
                                color = PokemonRojo,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // BOTÓN PRINCIPAL
                        Button(
                            onClick = { realizarLogin() },
                            enabled = !cargando,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PokemonRojo,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            if (cargando) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(23.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "¡COMENZAR AVENTURA!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "¡Hazte con todos!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

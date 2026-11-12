package com.tecsup.pokemon.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.R
import com.tecsup.pokemon.ui.UiState
import com.tecsup.pokemon.ui.components.Cargando

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onIrRegistro: () -> Unit,
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
    var claveVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val azul = Color(0xFF183B78)
    val dorado = Color(0xFFFFD54F)
    val rojo = Color(0xFFE93612)
    val crema = Color(0xFFFFFAED)

    val cargando = estado is UiState.Cargando

    val ingresar = {
        val email = correo.trim()

        correoError = when {
            email.isEmpty() -> "Ingresa tu correo electrónico"
            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches() ->
                "Ingresa un correo válido"
            else -> null
        }

        claveError = when {
            clave.isEmpty() -> "Ingresa tu contraseña"
            else -> null
        }

        if (
            correoError == null &&
            claveError == null &&
            !cargando
        ) {
            viewModel.iniciarSesion(email, clave)
        }
    }

    if (cargando && correo.isEmpty() && clave.isEmpty()) {
        Cargando(modifier)
    } else {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.fondo_login),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.16f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(24.dp))

                Image(
                    painter = painterResource(
                        R.drawable.logo_pokedex
                    ),
                    contentDescription = "Pokédex",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "¡BIENVENIDO, ENTRENADOR!",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Tu aventura Pokémon comienza aquí",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(22.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(azul.copy(alpha = 0.88f))
                        .border(
                            2.dp,
                            dorado.copy(alpha = 0.85f),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "INICIAR SESIÓN",
                        color = dorado,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Ingresa a tu cuenta de entrenador",
                        color = Color.White,
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(22.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            correoError = null
                            viewModel.limpiarError()
                        },
                        placeholder = {
                            Text("Correo electrónico", color = Color(0xFF64748B))
                        },
                        singleLine = true,
                        isError = correoError != null,
                        supportingText = correoError?.let { error ->
                            {
                                Text(
                                    error,
                                    color = Color(0xFFFFD6D6)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        colors = coloresCamposLogin(crema, azul),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clave,
                        onValueChange = {
                            clave = it
                            claveError = null
                            viewModel.limpiarError()
                        },
                        placeholder = {
                            Text("Contraseña", color = Color(0xFF64748B))
                        },
                        singleLine = true,
                        isError = claveError != null,
                        visualTransformation =
                            if (claveVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    claveVisible = !claveVisible
                                }
                            ) {
                                Icon(
                                    imageVector =
                                        if (claveVisible)
                                            Icons.Filled.VisibilityOff
                                        else
                                            Icons.Filled.Visibility,
                                    contentDescription =
                                        if (claveVisible)
                                            "Ocultar contraseña"
                                        else
                                            "Mostrar contraseña",
                                    tint = azul
                                )
                            }
                        },
                        supportingText = claveError?.let { error ->
                            {
                                Text(
                                    error,
                                    color = Color(0xFFFFD6D6)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { ingresar() }
                        ),
                        colors = coloresCamposLogin(crema, azul),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    (estado as? UiState.Error)?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = it.mensaje,
                            color = Color(0xFFFFD6D6),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(Modifier.height(22.dp))


                    Button(
                        onClick = { ingresar() },
                        enabled = !cargando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFFFD54F),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2456A6),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFF56749A)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 5.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "¡COMENZAR AVENTURA!",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }


                    Spacer(Modifier.height(14.dp))

                    TextButton(
                        onClick = {
                            viewModel.limpiarError()
                            onIrRegistro()
                        },
                        enabled = !cargando
                    ) {
                        Text(
                            "¿No tienes cuenta? Regístrate",
                            color = dorado,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "¡Hazte con todos!",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun coloresCamposLogin(
    fondo: Color,
    azul: Color
) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = azul,
    unfocusedTextColor = azul,
    focusedContainerColor = fondo,
    unfocusedContainerColor = fondo.copy(alpha = 0.95f),
    focusedPlaceholderColor = Color(0xFF64748B),
    unfocusedPlaceholderColor = Color(0xFF64748B),
    focusedBorderColor = Color(0xFFFFC83D),
    unfocusedBorderColor = azul.copy(alpha = 0.55f),
    cursorColor = azul,
    errorContainerColor = fondo,
    errorTextColor = azul,
    errorLabelColor = Color(0xFFB3261E),
    errorBorderColor = Color(0xFFE93612)
)


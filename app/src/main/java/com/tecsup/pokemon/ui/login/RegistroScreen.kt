package com.tecsup.pokemon.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.pokemon.R
import com.tecsup.pokemon.ui.UiState

private val AzulRegistro = Color(0xFF173B73)
private val DoradoRegistro = Color(0xFFFFCB05)

@Composable
fun RegistroScreen(
    viewModel: LoginViewModel,
    onVolverLogin: () -> Unit,
    modifier: Modifier = Modifier
) {

    val registroEstado by
    viewModel.registroEstado
        .collectAsStateWithLifecycle()

    var correo by rememberSaveable {
        mutableStateOf("")
    }

    var clave by rememberSaveable {
        mutableStateOf("")
    }

    var confirmarClave by rememberSaveable {
        mutableStateOf("")
    }

    var correoError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var claveError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var confirmarError by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var claveVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var confirmarVisible by rememberSaveable {
        mutableStateOf(false)
    }

    /*
     * Cuando el registro termina correctamente,
     * volvemos automáticamente al Login.
     */
    LaunchedEffect(registroEstado) {

        if (registroEstado is UiState.Exito) {

            viewModel.limpiarEstadoRegistro()

            onVolverLogin()
        }
    }

    val registrar = {

        var valido = true

        val correoLimpio =
            correo.trim()

        if (correoLimpio.isEmpty()) {

            correoError =
                "Ingresa tu correo electrónico"

            valido = false

        } else if (
            !android.util.Patterns
                .EMAIL_ADDRESS
                .matcher(correoLimpio)
                .matches()
        ) {

            correoError =
                "Ingresa un correo válido"

            valido = false

        } else {

            correoError = null
        }

        if (clave.isEmpty()) {

            claveError =
                "Ingresa una contraseña"

            valido = false

        } else if (clave.length < 6) {

            claveError =
                "La contraseña debe tener mínimo 6 caracteres"

            valido = false

        } else {

            claveError = null
        }

        if (confirmarClave.isEmpty()) {

            confirmarError =
                "Confirma tu contraseña"

            valido = false

        } else if (
            confirmarClave != clave
        ) {

            confirmarError =
                "Las contraseñas no coinciden"

            valido = false

        } else {

            confirmarError = null
        }

        if (valido) {

            viewModel.registrarUsuario(
                correoLimpio,
                clave
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Image(
            painter =
                painterResource(
                    R.drawable.fondo_login
                ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {

            Image(
                painter =
                    painterResource(
                        R.drawable.logo_pokedex
                    ),
                contentDescription =
                    "Logo Pokédex",
                contentScale =
                    ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
            )

            Surface(
                modifier =
                    Modifier.fillMaxWidth(),
                color =
                    AzulRegistro.copy(
                        alpha = 0.82f
                    ),
                shape =
                    RoundedCornerShape(24.dp),
                border =
                    androidx.compose.foundation
                        .BorderStroke(
                            2.dp,
                            DoradoRegistro
                        )
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text =
                            "¡NUEVO ENTRENADOR!",
                        style =
                            MaterialTheme.typography
                                .titleLarge,
                        fontWeight =
                            FontWeight.ExtraBold,
                        color =
                            DoradoRegistro
                    )

                    Spacer(
                        Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Crea tu cuenta y comienza tu aventura",
                        color =
                            Color.White,
                        style =
                            MaterialTheme.typography
                                .bodySmall
                    )

                    Spacer(
                        Modifier.height(22.dp)
                    )

                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            correoError = null
                        },
                        placeholder = {
                            Text(
                                "Correo electrónico"
                            )
                        },
                        singleLine = true,
                        isError =
                            correoError != null,
                        supportingText = {

                            correoError?.let {
                                Text(it)
                            }
                        },
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Email,
                                imeAction =
                                    ImeAction.Next
                            ),
                        colors =
                            campoRegistroColors(),
                        shape =
                            RoundedCornerShape(
                                14.dp
                            ),
                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    OutlinedTextField(
                        value = clave,
                        onValueChange = {
                            clave = it
                            claveError = null
                        },
                        placeholder = {
                            Text("Contraseña")
                        },
                        singleLine = true,
                        isError =
                            claveError != null,
                        supportingText = {

                            claveError?.let {
                                Text(it)
                            }
                        },
                        visualTransformation =
                            if (claveVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Password,
                                imeAction =
                                    ImeAction.Next
                            ),
                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    claveVisible =
                                        !claveVisible
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (
                                            claveVisible
                                        )
                                            Icons.Filled
                                                .VisibilityOff
                                        else
                                            Icons.Filled
                                                .Visibility,
                                    contentDescription =
                                        "Mostrar contraseña",
                                    tint =
                                        AzulRegistro
                                )
                            }
                        },
                        colors =
                            campoRegistroColors(),
                        shape =
                            RoundedCornerShape(
                                14.dp
                            ),
                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        Modifier.height(8.dp)
                    )

                    OutlinedTextField(
                        value =
                            confirmarClave,
                        onValueChange = {
                            confirmarClave = it
                            confirmarError = null
                        },
                        placeholder = {
                            Text(
                                "Confirmar contraseña"
                            )
                        },
                        singleLine = true,
                        isError =
                            confirmarError != null,
                        supportingText = {

                            confirmarError?.let {
                                Text(it)
                            }
                        },
                        visualTransformation =
                            if (
                                confirmarVisible
                            )
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Password,
                                imeAction =
                                    ImeAction.Done
                            ),
                        keyboardActions =
                            KeyboardActions(
                                onDone = {
                                    registrar()
                                }
                            ),
                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    confirmarVisible =
                                        !confirmarVisible
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (
                                            confirmarVisible
                                        )
                                            Icons.Filled
                                                .VisibilityOff
                                        else
                                            Icons.Filled
                                                .Visibility,
                                    contentDescription =
                                        "Mostrar contraseña",
                                    tint =
                                        AzulRegistro
                                )
                            }
                        },
                        colors =
                            campoRegistroColors(),
                        shape =
                            RoundedCornerShape(
                                14.dp
                            ),
                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    /*
                     * Error proveniente de Firebase
                     */
                    (registroEstado as? UiState.Error)
                        ?.let {

                            Spacer(
                                Modifier.height(6.dp)
                            )

                            Text(
                                text =
                                    it.mensaje,
                                color =
                                    Color.White,
                                style =
                                    MaterialTheme.typography
                                        .bodySmall
                            )
                        }

                    Spacer(
                        Modifier.height(14.dp)
                    )

                    Button(
                        onClick = registrar,
                        enabled =
                            registroEstado
                                    !is UiState.Cargando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape =
                            RoundedCornerShape(
                                14.dp
                            ),
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        Color(
                                            0xFF2D65B5
                                        ),
                                    contentColor =
                                        Color.White
                                )
                    ) {

                        if (
                            registroEstado
                                    is UiState.Cargando
                        ) {

                            CircularProgressIndicator(
                                modifier =
                                    Modifier.size(
                                        22.dp
                                    ),
                                color =
                                    Color.White,
                                strokeWidth =
                                    2.dp
                            )

                        } else {

                            Text(
                                text =
                                    "¡CREAR MI CUENTA!",
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }

                    Spacer(
                        Modifier.height(18.dp)
                    )

                    Text(
                        text =
                            "¿Ya tienes cuenta? Inicia sesión",
                        color =
                            DoradoRegistro,
                        fontWeight =
                            FontWeight.Bold,
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        modifier =
                            Modifier.clickable {

                                viewModel
                                    .limpiarEstadoRegistro()

                                onVolverLogin()
                            }
                    )
                }
            }

            Spacer(
                Modifier.height(18.dp)
            )

            Text(
                text =
                    "¡Tu aventura comienza aquí!",
                color = Color.White,
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

@Composable
private fun campoRegistroColors() =
    OutlinedTextFieldDefaults.colors(

        focusedContainerColor =
            Color(0xFFF8F4EC),

        unfocusedContainerColor =
            Color(0xFFF8F4EC),

        focusedTextColor =
            Color(0xFF173B73),

        unfocusedTextColor =
            Color(0xFF173B73),

        cursorColor =
            Color(0xFF173B73),

        focusedBorderColor =
            Color(0xFFFFCB05),

        unfocusedBorderColor =
            Color.Transparent,

        focusedPlaceholderColor =
            Color(0xFF173B73),

        unfocusedPlaceholderColor =
            Color(0xFF173B73),

        errorContainerColor =
            Color(0xFFF8F4EC),

        errorTextColor =
            Color(0xFF173B73)
    )
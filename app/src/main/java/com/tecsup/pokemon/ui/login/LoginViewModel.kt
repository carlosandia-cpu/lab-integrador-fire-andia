package com.tecsup.pokemon.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.data.auth.AuthRepository
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    /*
     * Estado de la sesión:
     *
     * Cargando
     * Exito(null) = no hay sesión
     * Exito(sesion) = usuario autenticado
     * Error = fallo de login
     */
    private val _estado =
        MutableStateFlow<UiState<Sesion?>>(
            UiState.Cargando
        )

    val estado: StateFlow<UiState<Sesion?>> =
        _estado.asStateFlow()

    /*
     * Estado específico del registro.
     *
     * No usamos _estado para el registro porque
     * registrar una cuenta NO debe iniciar sesión.
     */
    private val _registroEstado =
        MutableStateFlow<UiState<Unit>?>(
            null
        )

    val registroEstado:
            StateFlow<UiState<Unit>?> =
        _registroEstado.asStateFlow()

    init {

        viewModelScope.launch {

            val sesion =
                auth.sesionActual()

            _estado.value =
                UiState.Exito(sesion)
        }
    }

    fun iniciarSesion(
        correo: String,
        clave: String
    ) {

        if (
            correo.isBlank() ||
            clave.isBlank()
        ) {

            _estado.value =
                UiState.Error(
                    "Completa el correo y la contraseña"
                )

            return
        }

        _estado.value =
            UiState.Cargando

        viewModelScope.launch {

            auth.iniciarSesion(
                correo.trim(),
                clave
            )
                .onSuccess { sesion ->

                    _estado.value =
                        UiState.Exito(
                            sesion
                        )
                }
                .onFailure { error ->

                    _estado.value =
                        UiState.Error(
                            error.message
                                ?: "No se pudo iniciar sesión"
                        )
                }
        }
    }

    fun registrarUsuario(
        correo: String,
        clave: String
    ) {

        if (
            correo.isBlank() ||
            clave.isBlank()
        ) {

            _registroEstado.value =
                UiState.Error(
                    "Completa todos los campos"
                )

            return
        }

        _registroEstado.value =
            UiState.Cargando

        viewModelScope.launch {

            auth.registrarUsuario(
                correo.trim(),
                clave
            )
                .onSuccess {

                    /*
                     * Cuenta creada correctamente,
                     * pero NO iniciamos sesión.
                     */
                    _estado.value =
                        UiState.Exito(null)

                    _registroEstado.value =
                        UiState.Exito(Unit)
                }
                .onFailure { error ->

                    _registroEstado.value =
                        UiState.Error(
                            error.message
                                ?: "No se pudo registrar"
                        )
                }
        }
    }

    fun limpiarError() {

        if (_estado.value is UiState.Error) {

            _estado.value =
                UiState.Exito(null)
        }
    }

    fun limpiarEstadoRegistro() {
        _registroEstado.value = null
    }

    fun cerrarSesion() {

        auth.cerrarSesion()

        _estado.value =
            UiState.Exito(null)
    }
}
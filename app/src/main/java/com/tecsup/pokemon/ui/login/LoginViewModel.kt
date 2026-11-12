
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

    private val _estado =
        MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)

    val estado: StateFlow<UiState<Sesion?>> =
        _estado.asStateFlow()

    init {
        viewModelScope.launch {
            _estado.value = UiState.Exito(auth.sesionActual())
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _estado.value = UiState.Error(
                "Completa el correo y la contraseña"
            )
            return
        }

        if (_estado.value is UiState.Cargando) return

        _estado.value = UiState.Cargando

        viewModelScope.launch {
            auth.iniciarSesion(correo.trim(), clave)
                .onSuccess {
                    _estado.value = UiState.Exito(it)
                }
                .onFailure {
                    _estado.value = UiState.Error(
                        it.message ?: "No se pudo iniciar sesión"
                    )
                }
        }
    }

    fun registrarUsuario(
        correo: String,
        clave: String
    ) {
        if (correo.isBlank() || clave.isBlank()) {
            _estado.value = UiState.Error(
                "Completa todos los campos"
            )
            return
        }

        if (clave.length < 6) {
            _estado.value = UiState.Error(
                "La contraseña debe tener mínimo 6 caracteres"
            )
            return
        }

        if (_estado.value is UiState.Cargando) return

        _estado.value = UiState.Cargando

        viewModelScope.launch {
            auth.registrarUsuario(correo.trim(), clave)
                .onSuccess {
                    _estado.value = UiState.Exito(it)
                }
                .onFailure {
                    _estado.value = UiState.Error(
                        it.message ?: "No se pudo registrar"
                    )
                }
        }
    }

    fun limpiarError() {
        if (_estado.value is UiState.Error) {
            _estado.value = UiState.Exito(null)
        }
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _estado.value = UiState.Exito(null)
    }
}

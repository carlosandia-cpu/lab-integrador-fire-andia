package com.tecsup.pokemon.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Ficha
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetalleViewModel(
    private val repo: ItemRepository,
    private val id: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Ficha>>(UiState.Cargando)
    val estado: StateFlow<UiState<Ficha>> = _estado.asStateFlow()

    private val _shiny = MutableStateFlow(false)
    val shiny: StateFlow<Boolean> = _shiny.asStateFlow()

    init { cargar() }

    fun cargar() {
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            repo.obtenerFicha(id)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun alternarShiny() { _shiny.value = !_shiny.value }

    fun alternarFavorito() {
        viewModelScope.launch {
            repo.alternarFavorito(id)
            val actual = _estado.value
            if (actual is UiState.Exito) {
                _estado.value = UiState.Exito(actual.datos.copy(esFavorito = !actual.datos.esFavorito))
            }
        }
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }
}
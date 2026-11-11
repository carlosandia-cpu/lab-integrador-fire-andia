package com.tecsup.pokemon.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.notification.Notificador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class AdminDatos(val guardados: Int, val favoritos: Int)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _datos = MutableStateFlow(AdminDatos(0, 0))
    val datos: StateFlow<AdminDatos> = _datos.asStateFlow()

    private val _sincronizando = MutableStateFlow(false)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        // Los totales se recalculan cada vez que cambia la lista o los favoritos
        viewModelScope.launch { repo.items.collect { actualizar() } }
        viewModelScope.launch { repo.favoritos.collect { actualizar() } }
    }

    private suspend fun actualizar() {
        _datos.value = AdminDatos(repo.cantidadGuardada(), repo.cantidadFavoritos())
    }

    fun sincronizar() {
        viewModelScope.launch {
            _sincronizando.value = true
            _mensaje.value = null
            repo.refrescar()
                .onSuccess { n ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                    _mensaje.value = "Se sincronizaron $n elementos"
                }
                .onFailure { e -> _mensaje.value = mensajeDe(e) }
            _sincronizando.value = false
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repo.limpiarCache()
            _mensaje.value = "Caché limpiada"
        }
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }
}
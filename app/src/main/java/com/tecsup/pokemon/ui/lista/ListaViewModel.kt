
package com.tecsup.pokemon.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.Regiones
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado =
        MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)

    val estado: StateFlow<UiState<List<Item>>> =
        _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _tipo = MutableStateFlow<String?>(null)
    val tipo: StateFlow<String?> = _tipo.asStateFlow()

    private val _region =
        MutableStateFlow<Regiones.Region?>(null)
    val region: StateFlow<Regiones.Region?> =
        _region.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> =
        _busqueda.asStateFlow()

    private var refrescoTerminado = false
    private var errorSinDatos: String? = null
    private var trabajoRefresco: Job? = null

    // Lista filtrada por tipo, región y nombre.
    val visibles: StateFlow<UiState<List<Item>>> =
        combine(
            _estado,
            _tipo,
            _region,
            _busqueda
        ) { estado, tipo, region, texto ->

            if (estado is UiState.Exito) {
                UiState.Exito(
                    estado.datos.filter { item ->
                        (tipo == null || tipo in item.tipos) &&
                                (region == null ||
                                        item.id in region.rango) &&
                                item.titulo.contains(
                                    texto.trim(),
                                    ignoreCase = true
                                )
                    }
                )
            } else {
                estado
            }

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState.Cargando
        )

    init {
        // Room es la fuente de datos de la lista.
        viewModelScope.launch {
            repo.items.collect { lista ->

                when {
                    lista.isNotEmpty() -> {
                        errorSinDatos = null
                        _estado.value = UiState.Exito(lista)
                    }

                    errorSinDatos != null -> {
                        _estado.value =
                            UiState.Error(errorSinDatos!!)
                    }

                    refrescoTerminado -> {
                        _estado.value = UiState.Error(
                            "No hay Pokémon guardados. " +
                                    "Conéctate a internet y pulsa Reintentar."
                        )
                    }

                    else -> {
                        _estado.value = UiState.Cargando
                    }
                }
            }
        }

        refrescar()
    }

    fun refrescar() {
        // Evita sincronizaciones simultáneas.
        if (trabajoRefresco?.isActive == true) return

        trabajoRefresco = viewModelScope.launch {
            errorSinDatos = null
            _aviso.value = null

            try {
                val cantidadInicial = repo.cantidadGuardada()

                if (cantidadInicial == 0) {
                    _estado.value = UiState.Cargando
                }

                val resultado = repo.refrescar()
                refrescoTerminado = true

                resultado.onSuccess { cantidad ->
                    errorSinDatos = null
                    _aviso.value = null

                    if (cantidad == 0 &&
                        repo.cantidadGuardada() == 0
                    ) {
                        _estado.value = UiState.Error(
                            "No se descargaron Pokémon. " +
                                    "Pulsa Reintentar."
                        )
                    } else {
                        notificador.mostrar(
                            "Datos actualizados",
                            "Se guardaron $cantidad elementos"
                        )
                    }
                }

                resultado.onFailure { error ->
                    procesarError(error)
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                refrescoTerminado = true
                procesarError(e)
            }
        }
    }

    private suspend fun procesarError(error: Throwable) {
        val cantidad = repo.cantidadGuardada()

        if (cantidad == 0) {
            errorSinDatos = mensajeDe(error)
            _estado.value = UiState.Error(
                errorSinDatos!!
            )
        } else {
            errorSinDatos = null
            _aviso.value =
                "Sin conexión: mostrando datos guardados"
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            repo.alternarFavorito(id)
        }
    }

    fun elegirTipo(tipo: String?) {
        _tipo.value = tipo
    }

    fun elegirRegion(region: Regiones.Region?) {
        _region.value = region
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    private fun mensajeDe(e: Throwable): String =
        when (e) {
            is IOException ->
                "No hay conexión a internet"

            is HttpException ->
                "El servidor respondió con error ${e.code()}"

            else ->
                "Ocurrió un error inesperado"
        }
}

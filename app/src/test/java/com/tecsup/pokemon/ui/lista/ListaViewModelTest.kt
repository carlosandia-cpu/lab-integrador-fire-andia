
package com.tecsup.pokemon.ui.lista

import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.domain.model.Ficha
import com.tecsup.pokemon.domain.model.Item
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var repo: FakeRepository
    private lateinit var notificador: FakeNotificador

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
        repo = FakeRepository()
        notificador = FakeNotificador()
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun sinInternetNiCacheMuestraError() = runTest(dispatcher) {
        repo.error = IOException("Sin conexión")

        val vm = ListaViewModel(repo, notificador)

        advanceUntilIdle()

        assertTrue(vm.estado.value is UiState.Error)

        assertEquals(
            "No hay conexión a internet",
            (vm.estado.value as UiState.Error).mensaje
        )
    }

    @Test
    fun conCacheSinInternetConservaDatos() = runTest(dispatcher) {
        repo.datos.value = listOf(
            pokemon(1, "Bulbasaur")
        )

        repo.error = IOException("Sin conexión")

        val vm = ListaViewModel(repo, notificador)

        advanceUntilIdle()

        assertTrue(vm.estado.value is UiState.Exito)

        val estado = vm.estado.value as UiState.Exito

        assertEquals(1, estado.datos.size)

        assertEquals(
            "Sin conexión: mostrando datos guardados",
            vm.aviso.value
        )
    }

    @Test
    fun sincronizacionExitosaNotificaElementos() =
        runTest(dispatcher) {

            repo.datos.value = listOf(
                pokemon(1, "Bulbasaur")
            )

            val vm = ListaViewModel(repo, notificador)

            advanceUntilIdle()

            assertEquals(
                "Datos actualizados",
                notificador.titulo
            )

            assertEquals(
                "Se guardaron 1 elementos",
                notificador.texto
            )
        }

    private fun pokemon(
        id: Int,
        nombre: String
    ): Item {
        return Item(
            id = id,
            titulo = nombre,
            subtitulo = "#001",
            imagenUrl = null,
            esFavorito = false,
            tipos = listOf("grass")
        )
    }

    private class FakeNotificador : Notificador {

        var titulo = ""
        var texto = ""

        override fun mostrar(
            titulo: String,
            texto: String
        ) {
            this.titulo = titulo
            this.texto = texto
        }
    }

    private class FakeRepository : ItemRepository {

        val datos = MutableStateFlow<List<Item>>(emptyList())

        var error: Throwable? = null

        override val items: Flow<List<Item>> = datos

        override val favoritos: Flow<List<Item>> =
            MutableStateFlow(emptyList())

        override suspend fun refrescar(): Result<Int> {
            val fallo = error

            return if (fallo != null) {
                Result.failure(fallo)
            } else {
                Result.success(datos.value.size)
            }
        }

        override suspend fun obtenerDetalle(
            id: Int
        ): Result<Item> {
            return Result.failure(
                UnsupportedOperationException()
            )
        }

        override suspend fun obtenerFicha(
            id: Int
        ): Result<Ficha> {
            return Result.failure(
                UnsupportedOperationException()
            )
        }

        override suspend fun alternarFavorito(id: Int) {
            datos.value = datos.value.map { item ->
                if (item.id == id) {
                    item.copy(
                        esFavorito = !item.esFavorito
                    )
                } else {
                    item
                }
            }
        }

        override suspend fun cantidadGuardada(): Int {
            return datos.value.size
        }

        override suspend fun cantidadFavoritos(): Int {
            return datos.value.count {
                it.esFavorito
            }
        }

        override suspend fun limpiarCache() {
            datos.value = emptyList()
        }
    }
}

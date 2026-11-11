package com.tecsup.pokemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.ui.admin.AdminViewModel
import com.tecsup.pokemon.ui.detalle.DetalleViewModel
import com.tecsup.pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.pokemon.ui.lista.ListaViewModel
import com.tecsup.pokemon.ui.login.LoginViewModel
import com.tecsup.pokemon.ui.login.PerfilViewModel

class ViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(LoginViewModel::class.java) ->
            LoginViewModel(container.authRepository) as T
        modelClass.isAssignableFrom(ListaViewModel::class.java) ->
            ListaViewModel(container.itemRepository, container.notificador) as T
        modelClass.isAssignableFrom(FavoritosViewModel::class.java) ->
            FavoritosViewModel(container.itemRepository) as T
        modelClass.isAssignableFrom(AdminViewModel::class.java) ->
            AdminViewModel(container.itemRepository, container.notificador) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }

    fun paraDetalle(id: Int): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DetalleViewModel(container.itemRepository, id) as T
    }

    fun paraPerfil(uid: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PerfilViewModel(container.perfilRepository, uid) as T
    }
}
package com.tecsup.pokemon

import android.content.Context
import com.tecsup.pokemon.data.remote.RetrofitClient
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.data.repository.ItemRepositoryImpl
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.notification.NotificadorFalso

class AppContainer(context: Context) {
    val itemRepository: ItemRepository = ItemRepositoryImpl(RetrofitClient.api)
    val notificador: Notificador = NotificadorFalso()
}
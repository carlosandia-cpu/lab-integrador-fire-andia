package com.tecsup.pokemon

import android.content.Context
import com.tecsup.pokemon.data.local.AppDatabase
import com.tecsup.pokemon.data.remote.RetrofitClient
import com.tecsup.pokemon.data.repository.ItemRepository
import com.tecsup.pokemon.data.repository.ItemRepositoryImpl
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.notification.NotificadorFalso

class AppContainer(context: Context) {
    private val database = AppDatabase.obtener(context)

    val itemRepository: ItemRepository =
        ItemRepositoryImpl(RetrofitClient.api, database.itemDao())

    val notificador: Notificador = NotificadorFalso()
}
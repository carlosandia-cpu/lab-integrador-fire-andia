package com.tecsup.pokemon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.ui.ViewModelFactory
import com.tecsup.pokemon.ui.detalle.DetalleScreen
import com.tecsup.pokemon.ui.detalle.DetalleViewModel
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel

@Composable
fun AppNav(container: AppContainer, modifier: Modifier = Modifier) {
    val nav = rememberNavController()
    val factory = ViewModelFactory(container)

    NavHost(navController = nav, startDestination = "lista", modifier = modifier) {
        composable("lista") {
            val vm: ListaViewModel = viewModel(factory = factory)
            ListaScreen(
                viewModel = vm,
                onItemClick = { id -> nav.navigate("detalle/$id") }
            )
        }
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            val vm: DetalleViewModel = viewModel(
                key = "detalle-$id",
                factory = factory.paraDetalle(id)
            )
            DetalleScreen(viewModel = vm, onVolver = { nav.popBackStack() })
        }
    }
}
package com.tecsup.pokemon.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.ui.ViewModelFactory
import com.tecsup.pokemon.ui.detalle.DetalleScreen
import com.tecsup.pokemon.ui.detalle.DetalleViewModel
import com.tecsup.pokemon.ui.favoritos.FavoritosScreen
import com.tecsup.pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel

@Composable
fun AppNav(container: AppContainer, modifier: Modifier = Modifier) {
    val nav = rememberNavController()
    val factory = remember { ViewModelFactory(container) }
    val entrada by nav.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route
    val mostrarBarra = ruta == "lista" || ruta == "favoritos"

    Scaffold(
        modifier = modifier,
        contentWindowInsets = if (mostrarBarra) ScaffoldDefaults.contentWindowInsets
        else WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar {
                    NavigationBarItem(
                        selected = ruta == "lista",
                        onClick = { irA(nav, "lista") },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                        label = { Text("Pokédex") }
                    )
                    NavigationBarItem(
                        selected = ruta == "favoritos",
                        onClick = { irA(nav, "favoritos") },
                        icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                        label = { Text("Favoritos") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = "lista",
            modifier = Modifier.padding(padding)
        ) {
            composable("lista") {
                val vm: ListaViewModel = viewModel(factory = factory)
                ListaScreen(
                    viewModel = vm,
                    onItemClick = { id -> nav.navigate("detalle/$id") }
                )
            }
            composable("favoritos") {
                val vm: FavoritosViewModel = viewModel(factory = factory)
                FavoritosScreen(
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
}

private fun irA(nav: androidx.navigation.NavHostController, destino: String) {
    nav.navigate(destino) {
        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

package com.tecsup.pokemon.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.tecsup.pokemon.AppContainer
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.ViewModelFactory
import com.tecsup.pokemon.ui.admin.AdminScreen
import com.tecsup.pokemon.ui.admin.AdminViewModel
import com.tecsup.pokemon.ui.detalle.DetalleScreen
import com.tecsup.pokemon.ui.detalle.DetalleViewModel
import com.tecsup.pokemon.ui.favoritos.FavoritosScreen
import com.tecsup.pokemon.ui.favoritos.FavoritosViewModel
import com.tecsup.pokemon.ui.lista.ListaScreen
import com.tecsup.pokemon.ui.lista.ListaViewModel
import com.tecsup.pokemon.ui.login.PerfilScreen
import com.tecsup.pokemon.ui.login.PerfilViewModel
import com.tecsup.pokemon.ui.components.FondoPokemon

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)

@Composable
fun AppNav(
    container: AppContainer,
    sesion: Sesion,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nav = rememberNavController()

    val factory = remember(container) {
        ViewModelFactory(container)
    }

    val entrada by nav.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route

    val esAdmin = sesion.rol == Rol.ADMIN

    val mostrarBarra = ruta == "lista" ||
            ruta == "favoritos" ||
            ruta == "perfil" ||
            ruta == "admin"

    FondoPokemon(
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets,

            bottomBar = {
                if (mostrarBarra) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Azul.copy(alpha = 0.78f)
                            )
                            .navigationBarsPadding()
                            .padding(
                                horizontal = 4.dp,
                                vertical = 6.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.weight(1f)) {
                            TabPokemon(
                                seleccionado = ruta == "lista",
                                titulo = "Pokédex",
                                icono = Icons.AutoMirrored.Filled.List,
                                onClick = {
                                    irA(nav, "lista")
                                }
                            )
                        }

                        Box(Modifier.weight(1f)) {
                            TabPokemon(
                                seleccionado = ruta == "favoritos",
                                titulo = "Favoritos",
                                icono = Icons.Filled.Star,
                                onClick = {
                                    irA(nav, "favoritos")
                                }
                            )
                        }

                        if (esAdmin) {
                            Box(Modifier.weight(1f)) {
                                TabPokemon(
                                    seleccionado = ruta == "admin",
                                    titulo = "Admin",
                                    icono = Icons.Filled.Settings,
                                    onClick = {
                                        irA(nav, "admin")
                                    }
                                )
                            }
                        }

                        Box(Modifier.weight(1f)) {
                            TabPokemon(
                                seleccionado = ruta == "perfil",
                                titulo = "Cuenta",
                                icono = Icons.Filled.Person,
                                onClick = {
                                    irA(nav, "perfil")
                                }
                            )
                        }
                    }
                }
            }
        ) { padding ->

            NavHost(
                navController = nav,
                startDestination = "lista",
                modifier = Modifier.padding(padding)
            ) {

                // POKÉDEX
                composable("lista") {
                    val vm: ListaViewModel = viewModel(
                        factory = factory
                    )

                    ListaScreen(
                        viewModel = vm,
                        onItemClick = { id ->
                            nav.navigate("detalle/$id")
                        }
                    )
                }

                // FAVORITOS
                composable("favoritos") {
                    val vm: FavoritosViewModel = viewModel(
                        factory = factory
                    )

                    FavoritosScreen(
                        viewModel = vm,
                        onItemClick = { id ->
                            nav.navigate("detalle/$id")
                        }
                    )
                }

                // ADMINISTRADOR
                if (esAdmin) {
                    composable("admin") {
                        val vm: AdminViewModel = viewModel(
                            factory = factory
                        )

                        AdminScreen(
                            viewModel = vm
                        )
                    }
                }

                // CUENTA
                composable("perfil") {
                    val vm: PerfilViewModel = viewModel(
                        key = "perfil-${sesion.uid}",
                        factory = factory.paraPerfil(
                            sesion.uid
                        )
                    )

                    PerfilScreen(
                        sesion = sesion,
                        viewModel = vm,
                        onCerrarSesion = onCerrarSesion
                    )
                }

                // DETALLE
                composable(
                    route = "detalle/{id}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.IntType
                        }
                    )
                ) { entry ->

                    val id = entry.arguments
                        ?.getInt("id") ?: 0

                    val vm: DetalleViewModel = viewModel(
                        key = "detalle-$id",
                        factory = factory.paraDetalle(id)
                    )

                    DetalleScreen(
                        viewModel = vm,
                        onVolver = {
                            nav.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

// BARRA INFERIOR PERSONALIZADA
@Composable
private fun TabPokemon(
    seleccionado: Boolean,
    titulo: String,
    icono: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = if (seleccionado)
                Dorado.copy(alpha = 0.95f)
            else
                Color.Transparent,
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = if (seleccionado)
                    Azul
                else
                    Color.White,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 5.dp
                )
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = titulo,
            color = if (seleccionado)
                Dorado
            else
                Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

// NAVEGACIÓN SIN DUPLICAR PANTALLAS
private fun irA(
    nav: NavHostController,
    destino: String
) {
    nav.navigate(destino) {
        popUpTo(
            nav.graph.findStartDestination().id
        ) {
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
}

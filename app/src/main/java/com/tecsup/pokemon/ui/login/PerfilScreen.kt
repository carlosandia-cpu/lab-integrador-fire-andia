package com.tecsup.pokemon.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion

@Composable
fun PerfilScreen(
    sesion: Sesion,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi cuenta", style = MaterialTheme.typography.headlineSmall)
        Text(sesion.correo, modifier = Modifier.padding(top = 16.dp))
        Text(
            "Rol: " + if (sesion.rol == Rol.ADMIN) "Administrador" else "Usuario",
            modifier = Modifier.padding(top = 4.dp)
        )
        Button(onClick = onCerrarSesion, modifier = Modifier.padding(top = 24.dp)) {
            Text("Cerrar sesión")
        }
    }
}
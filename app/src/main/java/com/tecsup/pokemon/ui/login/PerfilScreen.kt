package com.tecsup.pokemon.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.Avatares

@Composable
fun PerfilScreen(
    sesion: Sesion,
    viewModel: PerfilViewModel,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val avatar by viewModel.avatar.collectAsStateWithLifecycle()
    var eligiendo by remember { mutableStateOf(false) }

    Column(
        modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = Avatares.url(avatar),
            contentDescription = "Foto de entrenador",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { eligiendo = true }
        )
        Text(
            "Toca la foto para cambiarla",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            "Mi cuenta",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(sesion.correo, modifier = Modifier.padding(top = 8.dp))
        Text(
            "Rol: " + if (sesion.rol == Rol.ADMIN) "Administrador" else "Usuario",
            modifier = Modifier.padding(top = 4.dp)
        )
        Button(onClick = onCerrarSesion, modifier = Modifier.padding(top = 24.dp)) {
            Text("Cerrar sesión")
        }
    }

    if (eligiendo) {
        AlertDialog(
            onDismissRequest = { eligiendo = false },
            title = { Text("Elige tu entrenador") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(320.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Avatares.todos) { id ->
                        val forma = RoundedCornerShape(12.dp)
                        AsyncImage(
                            model = Avatares.url(id),
                            contentDescription = id,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(forma)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .then(
                                    if (id == avatar)
                                        Modifier.border(3.dp, MaterialTheme.colorScheme.primary, forma)
                                    else Modifier
                                )
                                .clickable {
                                    viewModel.elegirAvatar(id)
                                    eligiendo = false
                                }
                        )
                    }
                }
            },
            confirmButton = { TextButton(onClick = { eligiendo = false }) { Text("Cerrar") } }
        )
    }
}
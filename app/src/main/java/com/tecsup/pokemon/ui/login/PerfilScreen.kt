package com.tecsup.pokemon.ui.login

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.ui.Avatares
import com.tecsup.pokemon.ui.components.FondoPokemon

private val Azul = Color(0xFF173B73)
private val Dorado = Color(0xFFFFCB05)

@Composable
fun PerfilScreen(sesion: Sesion, viewModel: PerfilViewModel, onCerrarSesion: () -> Unit, modifier: Modifier = Modifier) {
    val avatar by viewModel.avatar.collectAsStateWithLifecycle()
    var eligiendo by remember { mutableStateOf(false) }
    FondoPokemon(modifier = modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Surface(color = Azul.copy(alpha = 0.76f), shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)) {
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("TARJETA DE ENTRENADOR", color = Dorado, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(22.dp))
                    AsyncImage(
                        model = Avatares.url(avatar),
                        contentDescription = "Avatar del entrenador",
                        modifier = Modifier.size(130.dp).clip(CircleShape).background(Color.White)
                            .border(4.dp, Dorado, CircleShape).clickable { eligiendo = true }
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Cambiar entrenador", color = Color.White, style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.clickable { eligiendo = true })
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDCEAFF).copy(alpha = 0.76f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("MI CUENTA", color = Azul, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    HorizontalDivider()
                    Text("Correo electrónico", color = Azul)
                    Text(sesion.correo, color = Azul, fontWeight = FontWeight.SemiBold)
                    HorizontalDivider()
                    Text("Rango de entrenador", color = Azul)
                    Text(if (sesion.rol == Rol.ADMIN) "Administrador" else "Entrenador Pokémon", color = Azul, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Azul), shape = RoundedCornerShape(12.dp)) {
                        Text("CERRAR SESIÓN")
                    }
                }
            }
        }
    }
    if (eligiendo) {
        AlertDialog(
            onDismissRequest = { eligiendo = false },
            title = { Text("Elige tu entrenador") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), modifier = Modifier.height(320.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Avatares.todos) { id ->
                        val forma = RoundedCornerShape(12.dp)
                        AsyncImage(
                            model = Avatares.url(id), contentDescription = id,
                            modifier = Modifier.size(80.dp).clip(forma)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .then(if (id == avatar) Modifier.border(3.dp, Dorado, forma) else Modifier)
                                .clickable { viewModel.elegirAvatar(id); eligiendo = false }
                        )
                    }
                }
            },
            confirmButton = { TextButton(onClick = { eligiendo = false }) { Text("Cerrar") } }
        )
    }
}

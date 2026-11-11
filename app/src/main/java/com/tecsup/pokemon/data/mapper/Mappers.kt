package com.tecsup.pokemon.data.mapper

import com.tecsup.pokemon.data.local.ItemEntity
import com.tecsup.pokemon.data.remote.dto.PokemonRef
import com.tecsup.pokemon.domain.model.Item

private const val BASE_SPRITES =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon"

fun idDeUrl(url: String): Int =
    url.trimEnd('/').substringAfterLast('/').toInt()

// Dto -> Entity
fun PokemonRef.aEntity(tipos: List<String>, esFavorito: Boolean): ItemEntity {
    val id = idDeUrl(url)
    return ItemEntity(
        id = id,
        titulo = name,
        subtitulo = "#" + id.toString().padStart(3, '0'),
        imagenUrl = "$BASE_SPRITES/$id.png",
        esFavorito = esFavorito,
        tipos = tipos.joinToString(",")
    )
}

// Entity -> modelo de la UI
fun ItemEntity.aModelo(): Item = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito,
    tipos = if (tipos.isBlank()) emptyList() else tipos.split(",")
)
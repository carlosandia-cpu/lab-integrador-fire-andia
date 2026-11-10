package com.tecsup.pokemon.data.mapper

import com.tecsup.pokemon.data.remote.dto.PokemonRef
import com.tecsup.pokemon.domain.model.Item

private const val BASE_SPRITES =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon"

fun idDeUrl(url: String): Int =
    url.trimEnd('/').substringAfterLast('/').toInt()

fun PokemonRef.aItem(tipos: List<String> = emptyList()): Item {
    val id = idDeUrl(url)
    return Item(
        id = id,
        titulo = name,
        subtitulo = "#" + id.toString().padStart(3, '0'),
        imagenUrl = "$BASE_SPRITES/$id.png",
        tipos = tipos
    )
}
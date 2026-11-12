package com.tecsup.pokemon.ui

object Avatares {
    // Imágenes de entrenadores de Pokémon Showdown (crédito en el README)
    val todos = listOf(
        "red", "blue", "leaf", "ethan", "lyra", "brendan", "may", "cynthia",
        "steven", "lance", "oak", "misty", "brock", "erika", "sabrina", "giovanni",
        "blaine", "koga", "wallace", "flannery"
    )

    fun url(id: String) = "https://play.pokemonshowdown.com/sprites/trainers/$id.png"
}
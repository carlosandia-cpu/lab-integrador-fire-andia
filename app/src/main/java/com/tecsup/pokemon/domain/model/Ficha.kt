package com.tecsup.pokemon.domain.model

data class Ficha(
    val id: Int,
    val nombre: String,
    val alturaM: Double,
    val pesoKg: Double,
    val tipos: List<String>,
    val habilidades: List<String>,
    val stats: List<Pair<String, Int>>,
    val esFavorito: Boolean = false
) {
    val artworkUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    val artworkShinyUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/$id.png"
}
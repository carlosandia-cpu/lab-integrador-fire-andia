package com.tecsup.pokemon.ui

object Regiones {
    data class Region(val nombre: String, val rango: IntRange)

    val todas = listOf(
        Region("Kanto", 1..151),
        Region("Johto", 152..251),
        Region("Hoenn", 252..386),
        Region("Sinnoh", 387..493),
        Region("Teselia", 494..649),
        Region("Kalos", 650..721),
        Region("Alola", 722..809),
        Region("Galar", 810..905),
        Region("Paldea", 906..1025)
    )
}
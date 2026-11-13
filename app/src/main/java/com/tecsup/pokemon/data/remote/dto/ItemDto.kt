package com.tecsup.pokemon.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// =========================================================
// RESPUESTA DE LA LISTA DE POKÉMON
// GET /pokemon?limit=1025
// =========================================================

@Serializable
data class ListaResponse(
    val count: Int = 0,
    val next: String? = null,
    val previous: String? = null,
    val results: List<PokemonRef> = emptyList()
)

@Serializable
data class PokemonRef(
    val name: String,
    val url: String
)


// =========================================================
// RESPUESTA DE TIPOS
// GET /type/{tipo}
// =========================================================

@Serializable
data class TypeResponse(
    val pokemon: List<PokemonSlot> = emptyList()
)

@Serializable
data class PokemonSlot(
    val slot: Int = 1,
    val pokemon: PokemonRef
)


// =========================================================
// DETALLE DEL POKÉMON
// GET /pokemon/{id}
// =========================================================

@Serializable
data class PokemonDetalleDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,

    val types: List<TipoSlot> = emptyList(),

    val abilities: List<HabilidadSlot> = emptyList(),

    val stats: List<StatSlot> = emptyList()
)


// =========================================================
// TIPOS
// =========================================================

@Serializable
data class TipoSlot(
    val slot: Int = 1,
    val type: NombreRef
)


// =========================================================
// HABILIDADES
// =========================================================

@Serializable
data class HabilidadSlot(
    val ability: NombreRef
)


// =========================================================
// ESTADÍSTICAS
// =========================================================

@Serializable
data class StatSlot(

    @SerialName("base_stat")
    val baseStat: Int,

    val effort: Int = 0,

    val stat: NombreRef
)


// =========================================================
// REFERENCIA GENÉRICA DE NOMBRE
// =========================================================

@Serializable
data class NombreRef(
    val name: String,
    val url: String = ""
)
package com.tecsup.pokemon.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Respuesta de GET type/fire
@Serializable
data class TypeResponse(val pokemon: List<PokemonSlot>)

@Serializable
data class PokemonSlot(val pokemon: PokemonRef)

@Serializable
data class PokemonRef(val name: String, val url: String)

// Respuesta de GET pokemon/{nombre}
@Serializable
data class PokemonDetalleDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TipoSlot> = emptyList(),
    val stats: List<StatSlot> = emptyList()
)

@Serializable
data class TipoSlot(val type: NombreRef)

@Serializable
data class StatSlot(
    @SerialName("base_stat") val baseStat: Int,
    val stat: NombreRef
)

@Serializable
data class NombreRef(val name: String)
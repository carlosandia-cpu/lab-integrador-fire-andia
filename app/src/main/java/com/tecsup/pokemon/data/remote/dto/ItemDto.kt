package com.tecsup.pokemon.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// GET pokemon?limit=151
@Serializable
data class ListaResponse(val results: List<PokemonRef>)

@Serializable
data class PokemonRef(val name: String, val url: String)

// GET type/{tipo}
@Serializable
data class TypeResponse(val pokemon: List<PokemonSlot>)

@Serializable
data class PokemonSlot(val slot: Int = 1, val pokemon: PokemonRef)

// GET pokemon/{id}
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

@Serializable
data class TipoSlot(val type: NombreRef)

@Serializable
data class HabilidadSlot(val ability: NombreRef)

@Serializable
data class StatSlot(
    @SerialName("base_stat") val baseStat: Int,
    val stat: NombreRef
)

@Serializable
data class NombreRef(val name: String)
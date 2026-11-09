package com.tecsup.pokemon.data.remote

import com.tecsup.pokemon.data.remote.dto.PokemonDetalleDto
import com.tecsup.pokemon.data.remote.dto.TypeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("type/fire")
    suspend fun listar(): TypeResponse

    @GET("pokemon/{id}")
    suspend fun detalle(@Path("id") id: Int): PokemonDetalleDto
}
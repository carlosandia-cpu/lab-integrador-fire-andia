package com.tecsup.pokemon.data.remote

import com.tecsup.pokemon.data.remote.dto.ListaResponse
import com.tecsup.pokemon.data.remote.dto.PokemonDetalleDto
import com.tecsup.pokemon.data.remote.dto.TypeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun listar(@Query("limit") limit: Int = 151): ListaResponse

    @GET("type/{tipo}")
    suspend fun porTipo(@Path("tipo") tipo: String): TypeResponse

    @GET("pokemon/{id}")
    suspend fun detalle(@Path("id") id: Int): PokemonDetalleDto
}
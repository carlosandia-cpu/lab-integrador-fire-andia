package com.tecsup.pokemon.data.local

import androidx.room.Entity

@Entity(
    tableName = "favoritos",
    primaryKeys = ["uid", "pokemonId"]
)
data class FavoritoEntity(
    val uid: String,
    val pokemonId: Int
)
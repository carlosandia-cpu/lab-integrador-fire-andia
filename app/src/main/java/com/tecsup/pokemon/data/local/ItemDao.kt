package com.tecsup.pokemon.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    // =========================================================
    // POKÉMON
    // =========================================================

    @Query(
        """
        SELECT * 
        FROM items
        ORDER BY id
        """
    )
    fun todos(): Flow<List<ItemEntity>>

    @Query(
        """
        SELECT COUNT(*) 
        FROM items
        """
    )
    suspend fun contar(): Int

    @Upsert
    suspend fun guardarTodos(
        items: List<ItemEntity>
    )

    @Query(
        """
        DELETE FROM items
        """
    )
    suspend fun borrarTodo()

    // =========================================================
    // FAVORITOS POR USUARIO
    // =========================================================

    @Query(
        """
        SELECT i.*
        FROM items AS i
        INNER JOIN favoritos AS f
            ON i.id = f.pokemonId
        WHERE f.uid = :uid
        ORDER BY i.id
        """
    )
    fun favoritos(
        uid: String
    ): Flow<List<ItemEntity>>

    @Query(
        """
        SELECT pokemonId
        FROM favoritos
        WHERE uid = :uid
        """
    )
    fun idsFavoritos(
        uid: String
    ): Flow<List<Int>>

    @Query(
        """
        SELECT EXISTS(
            SELECT 1
            FROM favoritos
            WHERE uid = :uid
              AND pokemonId = :pokemonId
        )
        """
    )
    suspend fun esFavorito(
        uid: String,
        pokemonId: Int
    ): Boolean

    @Insert(
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun agregarFavorito(
        favorito: FavoritoEntity
    )

    @Query(
        """
        DELETE FROM favoritos
        WHERE uid = :uid
          AND pokemonId = :pokemonId
        """
    )
    suspend fun quitarFavorito(
        uid: String,
        pokemonId: Int
    )

    @Query(
        """
        SELECT COUNT(*)
        FROM favoritos
        WHERE uid = :uid
        """
    )
    suspend fun contarFavoritos(
        uid: String
    ): Int

    // Se usa al limpiar la caché desde Admin.
    @Query(
        """
        DELETE FROM favoritos
        """
    )
    suspend fun borrarTodosLosFavoritos()
}
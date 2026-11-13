package com.tecsup.pokemon.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ItemEntity::class,
        FavoritoEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {

        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(
            context: Context
        ): AppDatabase {

            return instancia
                ?: synchronized(this) {

                    instancia
                        ?: Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "pokedex.db"
                        )

                            /*
                             * Estamos desarrollando y los Pokémon
                             * pueden descargarse otra vez.
                             *
                             * Como cambió el esquema de Room,
                             * recreamos la BD local.
                             */
                            .fallbackToDestructiveMigration()

                            .build()
                            .also {
                                instancia = it
                            }
                }
        }
    }
}
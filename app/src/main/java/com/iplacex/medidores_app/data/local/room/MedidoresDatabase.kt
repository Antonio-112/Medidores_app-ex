package com.iplacex.medidores_app.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LecturaEntity::class], version = 1, exportSchema = false)
@TypeConverters(TipoMedidorConverter::class)
abstract class MedidoresDatabase : RoomDatabase() {
    abstract fun lecturaDao(): LecturaDao

    companion object {
        const val NAME = "medidores.db"
    }
}

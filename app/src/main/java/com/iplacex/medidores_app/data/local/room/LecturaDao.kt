package com.iplacex.medidores_app.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LecturaDao {
    @Query("SELECT * FROM lecturas ORDER BY fechaEpochDay DESC")
    fun getAll(): Flow<List<LecturaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lectura: LecturaEntity): Long

    @Delete
    suspend fun delete(lectura: LecturaEntity)
}

package com.iplacex.medidores_app.data.repository

import com.iplacex.medidores_app.domain.Lectura
import kotlinx.coroutines.flow.Flow

interface LecturaRepository {
    fun observarLecturas(): Flow<List<Lectura>>
    suspend fun guardar(lectura: Lectura): Long
    suspend fun eliminar(lectura: Lectura)
}

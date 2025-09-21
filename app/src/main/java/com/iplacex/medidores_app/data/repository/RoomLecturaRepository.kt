package com.iplacex.medidores_app.data.repository

import com.iplacex.medidores_app.data.local.room.LecturaDao
import com.iplacex.medidores_app.data.local.room.LecturaEntity
import com.iplacex.medidores_app.domain.Lectura
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLecturaRepository(
    private val lecturaDao: LecturaDao
) : LecturaRepository {

    override fun observarLecturas(): Flow<List<Lectura>> =
        lecturaDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun guardar(lectura: Lectura): Long =
        lecturaDao.insert(lectura.toEntity())

    override suspend fun eliminar(lectura: Lectura) {
        lecturaDao.delete(lectura.toEntity())
    }

    private fun LecturaEntity.toDomain(): Lectura =
        Lectura(
            id = id,
            medidorId = medidorId,
            tipo = tipo,
            fecha = LocalDate.ofEpochDay(fechaEpochDay),
            valor = valor
        )

    private fun Lectura.toEntity(): LecturaEntity =
        LecturaEntity(
            id = id,
            medidorId = medidorId,
            tipo = tipo,
            fechaEpochDay = fecha.toEpochDay(),
            valor = valor
        )
}

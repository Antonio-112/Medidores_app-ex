package com.iplacex.medidores_app.data

import com.iplacex.medidores_app.data.local.DatabaseConnection
import com.iplacex.medidores_app.data.local.InMemoryDatabaseConnection
import com.iplacex.medidores_app.data.repository.InMemoryLecturaRepository
import com.iplacex.medidores_app.data.repository.InMemoryMedidorRepository
import com.iplacex.medidores_app.data.repository.LecturaRepository
import com.iplacex.medidores_app.data.repository.MedidorRepository

/**
 * Punto único de creación de dependencias. Más adelante se sustituirá la implementación en memoria
 * por una basada en base de datos sin modificar el resto de la app.
 */
object ServiceLocator {
    private val connection: DatabaseConnection by lazy { InMemoryDatabaseConnection() }

    val medidorRepository: MedidorRepository by lazy { InMemoryMedidorRepository(connection) }
    val lecturaRepository: LecturaRepository by lazy { InMemoryLecturaRepository(connection) }
}

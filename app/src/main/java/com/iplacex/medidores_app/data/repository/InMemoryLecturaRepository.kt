package com.iplacex.medidores_app.data.repository

import com.iplacex.medidores_app.data.local.DatabaseConnection
import com.iplacex.medidores_app.domain.Lectura

class InMemoryLecturaRepository(
    private val connection: DatabaseConnection
) : LecturaRepository {

    override fun obtenerLecturas(): List<Lectura> = connection.obtenerLecturas()

    override fun guardar(lectura: Lectura) {
        connection.guardarLectura(lectura)
    }
}

package com.iplacex.medidores_app.data.repository

import com.iplacex.medidores_app.data.local.DatabaseConnection
import com.iplacex.medidores_app.domain.Medidor

class InMemoryMedidorRepository(
    private val connection: DatabaseConnection
) : MedidorRepository {

    override fun obtenerMedidores(): List<Medidor> = connection.obtenerMedidores()

    override fun buscarPorId(id: String): Medidor? =
        connection.obtenerMedidores().firstOrNull { it.id == id }

    override fun guardar(medidor: Medidor) {
        connection.guardarMedidor(medidor)
    }
}

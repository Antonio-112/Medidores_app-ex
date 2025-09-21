package com.iplacex.medidores_app.data.repository

import com.iplacex.medidores_app.domain.Medidor

interface MedidorRepository {
    fun obtenerMedidores(): List<Medidor>
    fun buscarPorId(id: String): Medidor?
    fun guardar(medidor: Medidor)
}

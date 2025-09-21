package com.iplacex.medidores_app.data.repository

import com.iplacex.medidores_app.domain.Medidor
import com.iplacex.medidores_app.domain.TipoMedidor

class DefaultMedidorRepository : MedidorRepository {

    private val medidores = mutableListOf(
        Medidor(id = "m1", alias = "Casa - Agua", tipo = TipoMedidor.AGUA),
        Medidor(id = "m2", alias = "Depto - Luz", tipo = TipoMedidor.LUZ),
        Medidor(id = "m3", alias = "Casa - Gas", tipo = TipoMedidor.GAS)
    )

    override fun obtenerMedidores(): List<Medidor> = medidores.toList()

    override fun buscarPorId(id: String): Medidor? =
        medidores.firstOrNull { it.id == id }

    override fun guardar(medidor: Medidor) {
        medidores.removeAll { it.id == medidor.id }
        medidores.add(medidor)
    }
}

package com.iplacex.medidores_app.data.local

import com.iplacex.medidores_app.domain.Lectura
import com.iplacex.medidores_app.domain.Medidor
import com.iplacex.medidores_app.domain.TipoMedidor
import java.time.LocalDate

/**
 * Implementación temporal en memoria. Permite probar la aplicación sin una base de datos real.
 */
class InMemoryDatabaseConnection : DatabaseConnection {

    private val medidores = mutableListOf(
        Medidor(id = "m1", alias = "Casa - Agua", tipo = TipoMedidor.AGUA),
        Medidor(id = "m2", alias = "Depto - Luz", tipo = TipoMedidor.LUZ),
        Medidor(id = "m3", alias = "Casa - Gas", tipo = TipoMedidor.GAS)
    )

    private val lecturas = mutableListOf(
        Lectura(
            medidorId = "m1",
            fecha = LocalDate.now().minusDays(3),
            valor = 123.4
        )
    )

    override fun obtenerMedidores(): List<Medidor> = medidores.toList()

    override fun guardarMedidor(medidor: Medidor) {
        medidores.removeAll { it.id == medidor.id }
        medidores.add(medidor)
    }

    override fun obtenerLecturas(): List<Lectura> = lecturas
        .sortedByDescending { it.fecha }
        .toList()

    override fun guardarLectura(lectura: Lectura) {
        lecturas.removeAll { it.id == lectura.id }
        lecturas.add(0, lectura)
    }
}

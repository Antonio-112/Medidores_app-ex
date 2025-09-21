package com.iplacex.medidores_app.data.local

import com.iplacex.medidores_app.domain.Lectura
import com.iplacex.medidores_app.domain.Medidor

/**
 * Contrato simple para representar la capa de conexión hacia la fuente de datos.
 * Más adelante se reemplazará por la conexión real a la base de datos.
 */
interface DatabaseConnection {
    fun obtenerMedidores(): List<Medidor>
    fun guardarMedidor(medidor: Medidor)
    fun obtenerLecturas(): List<Lectura>
    fun guardarLectura(lectura: Lectura)
}

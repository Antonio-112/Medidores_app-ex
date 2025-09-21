package com.iplacex.medidores_app.domain

import java.time.LocalDate

data class Lectura(
    val id: Long = 0,
    val medidorId: String,
    val tipo: TipoMedidor,
    val fecha: LocalDate,
    val valor: Double
)


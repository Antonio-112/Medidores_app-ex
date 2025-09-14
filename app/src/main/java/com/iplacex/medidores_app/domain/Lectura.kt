package com.iplacex.medidores_app.domain

import java.time.LocalDate
import java.util.UUID

data class Lectura(
    val id: String = UUID.randomUUID().toString(),
    val medidorId: String,
    val fecha: LocalDate,
    val valor: Double
)


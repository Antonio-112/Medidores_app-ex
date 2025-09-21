package com.iplacex.medidores_app.domain

import java.util.UUID

data class Medidor(
    val id: String = UUID.randomUUID().toString(),
    val alias: String,
    val tipo: TipoMedidor
)

package com.iplacex.medidores_app.domain

// Definición de la clase
enum class TipoMedidor {
    AGUA, LUZ, GAS
}

fun TipoMedidor.unidadMedida(): String = when (this) {
    TipoMedidor.AGUA, TipoMedidor.GAS -> "m³"
    TipoMedidor.LUZ -> "kWh"
}

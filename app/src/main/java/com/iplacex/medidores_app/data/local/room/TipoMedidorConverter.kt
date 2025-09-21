package com.iplacex.medidores_app.data.local.room

import androidx.room.TypeConverter
import com.iplacex.medidores_app.domain.TipoMedidor

class TipoMedidorConverter {
    @TypeConverter
    fun fromTipoMedidor(tipoMedidor: TipoMedidor): String = tipoMedidor.name

    @TypeConverter
    fun toTipoMedidor(value: String): TipoMedidor = TipoMedidor.valueOf(value)
}

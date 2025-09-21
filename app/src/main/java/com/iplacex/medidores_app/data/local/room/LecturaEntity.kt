package com.iplacex.medidores_app.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iplacex.medidores_app.domain.TipoMedidor

@Entity(tableName = "lecturas")
data class LecturaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medidorId: String,
    val tipo: TipoMedidor,
    val fechaEpochDay: Long,
    val valor: Double
)

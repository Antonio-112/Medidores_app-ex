package com.iplacex.medidores_app.ui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.iplacex.medidores_app.domain.TipoMedidor // usa tu enum existente

data class UiMedidor(val id: String, val alias: String, val tipo: TipoMedidor)
data class UiLectura(
    val id: String,
    val medidorId: String,
    val fecha: String,   // YYYY-MM-DD (texto, simple)
    val valor: String,   // texto; validaremos mínimo
    val unidad: String
)

data class UiState(
    val medidores: List<UiMedidor> = emptyList(),
    val lecturas: List<UiLectura> = emptyList(),
    // borrador del formulario
    val draftMedidorId: String? = null,
    val draftTipo: TipoMedidor? = null,
    val draftFecha: String = "",
    val draftValor: String = "",
    val draftUnidad: String = ""
)

class AppViewModel : ViewModel() {

    var uiState by mutableStateOf(
        UiState(
            medidores = listOf(
                UiMedidor("m1", "Casa - Agua", TipoMedidor.AGUA),
                UiMedidor("m2", "Depto - Luz", TipoMedidor.LUZ),
                UiMedidor("m3", "Casa - Gas",  TipoMedidor.GAS)
            ),
            draftMedidorId = "m1"
        )
    )
        private set

    fun updateDraft(
        medidorId: String? = null,
        tipo: TipoMedidor? = null,
        fecha: String? = null,
        valor: String? = null
    ) {
        val t = tipo ?: uiState.draftTipo
        uiState = uiState.copy(
            draftMedidorId = medidorId ?: uiState.draftMedidorId,
            draftTipo = t,
            draftFecha = fecha ?: uiState.draftFecha,
            draftValor = valor ?: uiState.draftValor,
            draftUnidad = when (t) {
                TipoMedidor.AGUA, TipoMedidor.GAS -> "m³"
                TipoMedidor.LUZ -> "kWh"
                null -> ""
            }
        )
    }

    fun saveDraft() {
        val mId = uiState.draftMedidorId ?: return
        val tipo = uiState.draftTipo ?: return
        if (uiState.draftFecha.isBlank() || uiState.draftValor.isBlank()) return

        val nueva = UiLectura(
            id = "lec_${System.currentTimeMillis()}",
            medidorId = mId,
            fecha = uiState.draftFecha,
            valor = uiState.draftValor,
            unidad = when (tipo) {
                TipoMedidor.AGUA, TipoMedidor.GAS -> "m³"
                TipoMedidor.LUZ -> "kWh"
            }
        )
        uiState = uiState.copy(
            lecturas = listOf(nueva) + uiState.lecturas,
            draftValor = "" // limpiar el campo valor
        )
    }
}

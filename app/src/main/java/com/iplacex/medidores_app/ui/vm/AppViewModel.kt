package com.iplacex.medidores_app.ui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.iplacex.medidores_app.data.ServiceLocator
import com.iplacex.medidores_app.data.repository.LecturaRepository
import com.iplacex.medidores_app.data.repository.MedidorRepository
import com.iplacex.medidores_app.domain.Lectura
import com.iplacex.medidores_app.domain.Medidor
import com.iplacex.medidores_app.domain.TipoMedidor
import com.iplacex.medidores_app.domain.unidadMedida
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeParseException

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

class AppViewModel(
    private val medidorRepository: MedidorRepository = ServiceLocator.medidorRepository,
    private val lecturaRepository: LecturaRepository = ServiceLocator.lecturaRepository
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        cargarDatosIniciales()
    }

    private fun cargarDatosIniciales() {
        val medidores = medidorRepository.obtenerMedidores()
        val lecturas = lecturaRepository.obtenerLecturas()
        val medidorInicial = medidores.firstOrNull()
        uiState = UiState(
            medidores = medidores.map { it.toUi() },
            lecturas = lecturas.map { it.toUi() },
            draftMedidorId = medidorInicial?.id,
            draftTipo = medidorInicial?.tipo,
            draftFecha = LocalDate.now().toString(),
            draftUnidad = medidorInicial?.tipo?.unidadMedida().orEmpty()
        )
    }

    fun updateDraft(
        medidorId: String? = null,
        tipo: TipoMedidor? = null,
        fecha: String? = null,
        valor: String? = null
    ) {
        val nuevoMedidorId = medidorId ?: uiState.draftMedidorId
        val medidorSeleccionado = nuevoMedidorId?.let { id ->
            uiState.medidores.firstOrNull { it.id == id }
        }
        val nuevoTipo = when {
            tipo != null -> tipo
            medidorId != null -> medidorSeleccionado?.tipo
            else -> uiState.draftTipo
        }
        uiState = uiState.copy(
            draftMedidorId = nuevoMedidorId,
            draftTipo = nuevoTipo,
            draftFecha = fecha ?: uiState.draftFecha,
            draftValor = valor ?: uiState.draftValor,
            draftUnidad = nuevoTipo?.unidadMedida().orEmpty()
        )
    }

    fun saveDraft() {
        val medidorId = uiState.draftMedidorId ?: return
        val tipo = uiState.draftTipo ?: return
        val fechaTexto = uiState.draftFecha.trim()
        val valorTexto = uiState.draftValor.trim()
        if (fechaTexto.isBlank() || valorTexto.isBlank()) return

        val fecha = try {
            LocalDate.parse(fechaTexto)
        } catch (ex: DateTimeParseException) {
            return
        }
        val valor = valorTexto.replace(',', '.').toDoubleOrNull() ?: return

        val nuevaLectura = Lectura(
            medidorId = medidorId,
            fecha = fecha,
            valor = valor
        )
        lecturaRepository.guardar(nuevaLectura)
        val lecturasActualizadas = lecturaRepository.obtenerLecturas().map { it.toUi() }
        uiState = uiState.copy(
            lecturas = lecturasActualizadas,
            draftFecha = LocalDate.now().toString(),
            draftValor = "",
            draftUnidad = tipo.unidadMedida()
        )
    }

    private fun Medidor.toUi() = UiMedidor(id = id, alias = alias, tipo = tipo)

    private fun Lectura.toUi(): UiLectura {
        val medidor = medidorRepository.buscarPorId(medidorId)
        val unidad = medidor?.tipo?.unidadMedida().orEmpty()
        return UiLectura(
            id = id,
            medidorId = medidorId,
            fecha = fecha.toString(),
            valor = BigDecimal.valueOf(valor).stripTrailingZeros().toPlainString(),
            unidad = unidad
        )
    }
}

package com.iplacex.medidores_app.ui.vm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiMedidor(val id: String, val alias: String, val tipo: TipoMedidor)
data class UiLectura(
    val id: Long,
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

@RequiresApi(Build.VERSION_CODES.O)
class AppViewModel(
    private val medidorRepository: MedidorRepository = ServiceLocator.medidorRepository,
    private val lecturaRepository: LecturaRepository = ServiceLocator.lecturaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        cargarDatosIniciales()
        observarLecturas()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cargarDatosIniciales() {
        val medidores = medidorRepository.obtenerMedidores()
        val medidorInicial = medidores.firstOrNull()
        _uiState.update {
            it.copy(
                medidores = medidores.map { medidor -> medidor.toUi() },
                draftMedidorId = medidorInicial?.id,
                draftTipo = medidorInicial?.tipo,
                draftFecha = LocalDate.now().toString(),
                draftUnidad = medidorInicial?.tipo?.unidadMedida().orEmpty()
            )
        }
    }

    private fun observarLecturas() {
        viewModelScope.launch {
            lecturaRepository.observarLecturas()
                .collect { lecturas ->
                    _uiState.update { state ->
                        state.copy(lecturas = lecturas.map { it.toUi() })
                    }
                }
        }
    }

    fun updateDraft(
        medidorId: String? = null,
        tipo: TipoMedidor? = null,
        fecha: String? = null,
        valor: String? = null
    ) {
        val currentState = _uiState.value
        val nuevoMedidorId = medidorId ?: currentState.draftMedidorId
        val medidorSeleccionado = nuevoMedidorId?.let { id ->
            currentState.medidores.firstOrNull { it.id == id }
        }
        val nuevoTipo = when {
            tipo != null -> tipo
            medidorId != null -> medidorSeleccionado?.tipo
            else -> currentState.draftTipo
        }
        _uiState.update {
            it.copy(
                draftMedidorId = nuevoMedidorId,
                draftTipo = nuevoTipo,
                draftFecha = fecha ?: it.draftFecha,
                draftValor = valor ?: it.draftValor,
                draftUnidad = nuevoTipo?.unidadMedida().orEmpty()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDraft() {
        val currentState = _uiState.value
        val medidorId = currentState.draftMedidorId ?: return
        val tipo = currentState.draftTipo ?: return
        val fechaTexto = currentState.draftFecha.trim()
        val valorTexto = currentState.draftValor.trim()
        if (fechaTexto.isBlank() || valorTexto.isBlank()) return

        val fecha = try {
            LocalDate.parse(fechaTexto)
        } catch (ex: DateTimeParseException) {
            return
        }
        val valor = valorTexto.replace(',', '.').toDoubleOrNull() ?: return

        val nuevaLectura = Lectura(
            medidorId = medidorId,
            tipo = tipo,
            fecha = fecha,
            valor = valor
        )
        viewModelScope.launch {
            lecturaRepository.guardar(nuevaLectura)
            _uiState.update {
                it.copy(
                    draftFecha = LocalDate.now().toString(),
                    draftValor = ""
                )
            }
        }
    }

    private fun Medidor.toUi() = UiMedidor(id = id, alias = alias, tipo = tipo)

    private fun Lectura.toUi(): UiLectura {
        val medidor = medidorRepository.buscarPorId(medidorId)
        val unidad = tipo.unidadMedida().ifBlank {
            medidor?.tipo?.unidadMedida().orEmpty()
        }
        return UiLectura(
            id = id,
            medidorId = medidorId,
            fecha = fecha.toString(),
            valor = BigDecimal.valueOf(valor).stripTrailingZeros().toPlainString(),
            unidad = unidad
        )
    }
}

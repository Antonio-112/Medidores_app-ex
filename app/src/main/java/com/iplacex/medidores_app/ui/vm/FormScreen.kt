package com.iplacex.medidores_app.ui.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
// import androidx.compose.ui.text.input.KeyboardOptions
// import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.iplacex.medidores_app.domain.TipoMedidor
import com.iplacex.medidores_app.ui.vm.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    state: UiState,
    onValueChange: (
        medidorId: String?, tipo: TipoMedidor?, fecha: String?, valor: String?
    ) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Nueva lectura") }) }
    ) { inner ->
        Column(
            Modifier.padding(inner).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Medidor (dropdown simple)
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = state.medidores.firstOrNull { it.id == state.draftMedidorId }?.alias ?: "",
                    onValueChange = {},
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    label = { Text("Medidor") },
                    readOnly = true
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    state.medidores.forEach { m ->
                        DropdownMenuItem(
                            text = { Text(m.alias) },
                            onClick = {
                                expanded = false
                                onValueChange(m.id, m.tipo, null, null)
                            }
                        )
                    }
                }
            }

            // Tipo (chips)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TipoMedidor.values().forEach { t ->
                    FilterChip(
                        selected = state.draftTipo == t,
                        onClick = { onValueChange(null, t, null, null) },
                        label = { Text(t.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            OutlinedTextField(
                value = state.draftFecha,
                onValueChange = { onValueChange(null, null, it, null) },
                label = { Text("Fecha (YYYY-MM-DD)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            val valorLabel = if (state.draftUnidad.isBlank()) {
                "Valor"
            } else {
                "Valor (${state.draftUnidad})"
            }
            OutlinedTextField(
                value = state.draftValor,
                onValueChange = { onValueChange(null, null, null, it) },
                label = { Text(valorLabel) },
                //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onCancel) { Text("Cancelar") }
                Button(
                    onClick = onSave,
                    enabled = state.draftMedidorId != null &&
                            state.draftTipo != null &&
                            state.draftFecha.isNotBlank() &&
                            state.draftValor.isNotBlank()
                ) { Text("Guardar") }
            }
        }
    }
}

package com.iplacex.medidores_app.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iplacex.medidores_app.R
import com.iplacex.medidores_app.ui.vm.UiLectura
import com.iplacex.medidores_app.ui.vm.UiMedidor
import com.iplacex.medidores_app.ui.vm.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: UiState,
    onAddClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.list_top_bar_title)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.list_add_content_description))
            }
        }
    ) { inner ->
        Box(Modifier.padding(inner).fillMaxSize()) {
            if (state.lecturas.isEmpty()) {
                Text(stringResource(R.string.list_empty_state), modifier = Modifier.padding(24.dp))
            } else {
                Lista(state.lecturas, state.medidores.associateBy { it.id })
            }
        }
    }
}

@Composable
private fun Lista(lecturas: List<UiLectura>, medidores: Map<String, UiMedidor>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lecturas) { l ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    val m = medidores[l.medidorId]
                    Text(m?.alias ?: stringResource(R.string.list_default_meter_name), fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    val unidad = l.unidad
                    val unidadTexto = if (unidad.isBlank()) "" else " ${unidad}"
                    Text(stringResource(R.string.list_reading_details, l.fecha, l.valor, unidadTexto))
                }
            }
        }
    }
}

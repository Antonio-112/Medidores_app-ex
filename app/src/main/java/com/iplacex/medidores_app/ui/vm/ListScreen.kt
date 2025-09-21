package com.iplacex.medidores_app.ui.vm

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iplacex.medidores_app.R
import com.iplacex.medidores_app.domain.TipoMedidor

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val iconRes = m?.tipo?.let { tipoIconRes(it) }
                        if (iconRes != null) {
                            Icon(
                                painter = painterResource(iconRes),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                        Text(
                            m?.alias ?: stringResource(R.string.list_default_meter_name),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    val unidad = l.unidad
                    val unidadTexto = if (unidad.isBlank()) "" else " ${unidad}"
                    Text(stringResource(R.string.list_reading_details, l.fecha, l.valor, unidadTexto))
                }
            }
        }
    }
}

@DrawableRes
private fun tipoIconRes(tipoMedidor: TipoMedidor): Int = when (tipoMedidor) {
    TipoMedidor.AGUA -> R.drawable.ic_agua
    TipoMedidor.LUZ -> R.drawable.ic_luz
    TipoMedidor.GAS -> R.drawable.ic_gas
}

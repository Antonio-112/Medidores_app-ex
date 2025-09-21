package com.iplacex.medidores_app.data.repository

import com.iplacex.medidores_app.domain.Lectura

interface LecturaRepository {
    fun obtenerLecturas(): List<Lectura>
    fun guardar(lectura: Lectura)
}

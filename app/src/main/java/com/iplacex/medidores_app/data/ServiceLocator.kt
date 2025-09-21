package com.iplacex.medidores_app.data

import android.content.Context
import androidx.room.Room
import com.iplacex.medidores_app.data.local.room.MedidoresDatabase
import com.iplacex.medidores_app.data.repository.DefaultMedidorRepository
import com.iplacex.medidores_app.data.repository.LecturaRepository
import com.iplacex.medidores_app.data.repository.MedidorRepository
import com.iplacex.medidores_app.data.repository.RoomLecturaRepository

/**
 * Punto único de creación de dependencias. Expone repositorios respaldados por Room y
 * recursos en memoria según corresponda.
 */
object ServiceLocator {
    private lateinit var applicationContext: Context

    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    private val database: MedidoresDatabase by lazy {
        check(::applicationContext.isInitialized) { "ServiceLocator no inicializado" }
        Room.databaseBuilder(
            applicationContext,
            MedidoresDatabase::class.java,
            MedidoresDatabase.NAME
        ).build()
    }

    val medidorRepository: MedidorRepository by lazy { DefaultMedidorRepository() }

    val lecturaRepository: LecturaRepository by lazy {
        RoomLecturaRepository(database.lecturaDao())
    }
}

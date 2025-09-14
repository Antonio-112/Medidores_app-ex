package com.iplacex.medidores_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iplacex.medidores_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var vb: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
    }
}

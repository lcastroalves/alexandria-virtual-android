package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdmTelaEventos : AppCompatActivity() {
    private lateinit var btnVoltar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adm_tela_eventos)

        btnVoltar = findViewById(R.id.botaoVoltar2)
    }
}
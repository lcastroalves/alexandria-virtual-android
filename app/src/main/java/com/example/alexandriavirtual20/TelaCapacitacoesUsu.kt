package com.example.alexandriavirtual20

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageButton

class TelaCapacitacoesUsu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_capacitacoes_usu)

        val botaoVoltar = findViewById<ImageButton>(R.id.botaoVoltar)
        botaoVoltar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
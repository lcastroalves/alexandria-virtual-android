package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaInfoAtividadeUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_info_atividade_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
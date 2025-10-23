package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaInfoEventoUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnNotificar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_info_evento_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnNotificar = findViewById(R.id.botaoNotificar)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnNotificar.setOnClickListener {
            Toast.makeText(this, "Você será notificado!", Toast.LENGTH_SHORT).show()
        }
    }
}
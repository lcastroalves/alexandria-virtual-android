package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class TelaConfirmReservaUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_confirm_reserva_usu)

        btnVoltar = findViewById(R.id.botaoVoltar7)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
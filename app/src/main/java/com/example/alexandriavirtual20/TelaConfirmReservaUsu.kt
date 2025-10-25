package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class TelaConfirmReservaUsu : AppCompatActivity() {
    private lateinit var btnConfirmar: Button
    private lateinit var btnVoltar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_confirm_reserva_usu)

        btnVoltar = findViewById(R.id.botaoVoltar7)
        btnConfirmar = findViewById(R.id.Confirmar)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnConfirmar.setOnClickListener {
//           val intent = Intent(this, TelaReservaCabUsu::class.java)
//            startActivity(intent)
        }
    }
}
package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TelaConfirmReservaUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var tvCabine: TextView
    private lateinit var tvDataHora: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_confirm_reserva_usu)

        btnVoltar = findViewById(R.id.botaoVoltar7)
        tvCabine = findViewById(R.id.tvCab)
        tvDataHora = findViewById(R.id.tvDataHora)

        val data = intent.getStringExtra("data")
        val periodo = intent.getStringExtra("periodo")
        val numero = intent.getStringExtra("cabine")

        tvCabine.text = "Cabine $numero reservada!"
        tvDataHora.text = "$data | $periodo"

        btnVoltar.setOnClickListener {
            val intent = Intent(this, AMain::class.java)
            intent.putExtra("fragment_destino", "reserva")
            startActivity(intent)
        }
    }
}
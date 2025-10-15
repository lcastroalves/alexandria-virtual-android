package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TelaReservaCabUsu : AppCompatActivity() {

    lateinit var textData: TextView
    private lateinit var textHora: TextView
    private lateinit var btnTrocarHorario: Button
    private lateinit var btnConfirmar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_reserva_cab_usu)

        textData = findViewById(R.id.TextoData)
        textHora = findViewById(R.id.TextoHora)
        btnTrocarHorario  = findViewById(R.id.btnTrocarHorario)
        btnConfirmar = findViewById(R.id.Confirmar)

        val dataAtual = LocalDate.now()
        val horaAtual = LocalTime.now()

        val formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("pt-br"))
        val formatoHora = DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("pt-br"))

        textData.text = dataAtual.format(formatoData)
        textHora.text = (horaAtual.format(formatoHora) + " - " + horaAtual.plusHours(2))

        btnTrocarHorario.setOnClickListener {
            val intent = Intent(this, TelaHoraCabineUsu::class.java )
            startActivity(intent)
        }

        btnConfirmar.setOnClickListener {
            val intent = Intent(this, TelaCabDisponUsu::class.java )
            startActivity(intent)
        }

    }
}
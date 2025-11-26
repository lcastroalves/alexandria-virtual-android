package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.MaterialColors
import java.util.Locale

class TelaHoraCabineUsu : AppCompatActivity() {
    private lateinit var btnConfirmar: Button
    private lateinit var btnVoltar: ImageButton
    private lateinit var npHora: NumberPicker
    private lateinit var npMinuto: NumberPicker

    private val stepsMinutos by lazy {
        (0..55 step 5).map { String.format("%02d", it) }.toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_hora_cabine_usu)

        npHora = findViewById(R.id.npHora)
        npMinuto = findViewById(R.id.npMinuto)
        btnConfirmar = findViewById(R.id.Confirmar)
        btnVoltar = findViewById(R.id.botaoVoltar5)

        setupPickers()
        setupButton()
    }

    private fun setupPickers() {
        npHora.minValue = 0
        npHora.maxValue = 23
        npHora.setFormatter { String.format("%02d", it) }

        npMinuto.displayedValues = null
        npMinuto.minValue = 0
        npMinuto.maxValue = stepsMinutos.lastIndex
        npMinuto.displayedValues = stepsMinutos

        npHora.value = 10
        npMinuto.value = stepsMinutos.indexOf("30").coerceAtLeast(0)

        styleNumberPicker(npHora)
        styleNumberPicker(npMinuto)
    }

    private fun setupButton() {
        btnConfirmar.setOnClickListener {
            val dataFormatada = intent.getStringExtra("dataFormatada")

            val hora = npHora.value
            val minutoSelecionado = stepsMinutos[npMinuto.value]
            val minuto = minutoSelecionado.toIntOrNull() ?: 0

            val inicio = java.time.LocalTime.of(hora, minuto)
            val fim = inicio.plusHours(2)

            val fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("pt-BR"))
            val inicioStr = inicio.format(fmt)
            val fimStr = fim.format(fmt)
            val periodo = "$inicioStr - $fimStr"

            val agora = java.time.LocalTime.now()
            if (inicio.isBefore(agora)) {
                Toast.makeText(this, "Escolha um horário a partir do horário atual.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, TelaCabDisponUsu::class.java)
            intent.putExtra("dataFormatada", dataFormatada)
            intent.putExtra("periodo", periodo)
            intent.putExtra("inicio", inicioStr)
            intent.putExtra("fim", fimStr)
            startActivity(intent)
        }
        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    @Suppress("SoonBlockedPrivateApi")
    private fun styleNumberPicker(np: NumberPicker) {
        // texto
        for (i in 0 until np.childCount) {
            val v = np.getChildAt(i)
            if (v is EditText) {
                v.textSize = 20f
                v.setTextColor(
                    MaterialColors.getColor(v, com.google.android.material.R.attr.colorOnSurface)
                )
            }
        }
    }
}





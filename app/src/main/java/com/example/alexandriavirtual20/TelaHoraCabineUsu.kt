package com.example.alexandriavirtual20

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.MaterialColors

class TelaHoraCabineUsu : AppCompatActivity() {
    private lateinit var btnConfirmar: Button
    private lateinit var btnVoltar: Button
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

        setupPickers()
        setupButton()
    }

    private fun setupPickers() {
        // Hora 0..23
        npHora.minValue = 0
        npHora.maxValue = 23
        npHora.setFormatter { String.format("%02d", it) }

        // Minuto com step (00, 05, ..., 55)
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
            val intent = Intent(this, TelaCabDisponUsu::class.java)
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
                    MaterialColors.getColor(
                        v,
                        com.google.android.material.R.attr.colorOnSurface
                    )
                )
            }
        }
        try {
            val f = NumberPicker::class.java.getDeclaredField("mSelectionDivider")
            f.isAccessible = true
            f.set(np, ColorDrawable(Color.TRANSPARENT))
            np.invalidate()
        } catch (_: Exception) {  }
    }
}





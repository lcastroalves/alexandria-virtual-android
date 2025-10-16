package com.example.alexandriavirtual20

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors

class AdmTelaHoraCab : Fragment() {
    private lateinit var btnConfirmar: Button
    private lateinit var btnVoltar: Button
    private lateinit var npHora: NumberPicker
    private lateinit var npMinuto: NumberPicker

    private val stepsMinutos by lazy {
        (0..55 step 5).map { String.format("%02d", it) }.toTypedArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tela_reserva_cab_usu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        npHora = view.findViewById(R.id.npHora)
        npMinuto = view.findViewById(R.id.npMinuto)
        btnConfirmar = view.findViewById(R.id.btnConfirmar)

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
            val intent = Intent(requireContext(), TelaCabDisponUsu::class.java)
            startActivity(intent)
        }
        btnVoltar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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





package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdmTelaCabReserv : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var textData: TextView
    private lateinit var textHora: TextView
    private lateinit var btnTrocarHorario: Button
    private lateinit var btnConfirmar: Button
    private lateinit var btnDiaCompleto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.adm_tela_cab_reserv, container, false)

        textData = view.findViewById(R.id.TextoData)
        textHora = view.findViewById(R.id.TextoHora)
        btnTrocarHorario = view.findViewById(R.id.btnTrocarHorario)
        btnConfirmar = view.findViewById(R.id.Confirmar)
        btnDiaCompleto = view.findViewById(R.id.btnDiaCompleto)

        val dataAtual = LocalDate.now()
        val horaAtual = LocalTime.now()

        val formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))
        val formatoHora = DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("pt-BR"))

        textData.text = dataAtual.format(formatoData)
        textHora.text = (horaAtual.format(formatoHora) + " - " + horaAtual.plusHours(2))

        // Ações dos botões
        btnTrocarHorario.setOnClickListener {
            val intent = Intent(requireContext(), AdmTelaHoraCab::class.java)
            startActivity(intent)
        }

        btnConfirmar.setOnClickListener {
            val intent = Intent(requireContext(), AdmTelaCabReservHorEsp::class.java)
            startActivity(intent)
        }

        btnDiaCompleto.setOnClickListener {
            val intent = Intent(requireContext(), AdmTelaCabReservComNome::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TelaReservaCabUsu().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
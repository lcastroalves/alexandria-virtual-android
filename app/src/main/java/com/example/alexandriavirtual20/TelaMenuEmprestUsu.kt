package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class TelaMenuEmprestUsu : Fragment() {

    private lateinit var btnEmprestimo: LinearLayout
    private lateinit var btnMeusLivros: LinearLayout
    private lateinit var btnHistoricoLeit: LinearLayout
    private lateinit var btnSolicPendentes: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Conecta o layout XML (tela_menu_emprest_usu.xml) ao fragmento
        return inflater.inflate(R.layout.tela_menu_emprest_usu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnEmprestimo = view.findViewById(R.id.btnEmprestimo)
        btnMeusLivros = view.findViewById(R.id.btnMeusLivros)
        btnHistoricoLeit = view.findViewById(R.id.btnHistoricoLeit)
        btnSolicPendentes = view.findViewById(R.id.btnSolicPendentes)

        btnEmprestimo.setOnClickListener {
            trocarParaTelaEmprestimoDeLivros()
        }
        btnMeusLivros.setOnClickListener {
            trocarParaTelaMeusLivros()
        }
        btnHistoricoLeit.setOnClickListener {
            trocarParaTelaHistoricoLeitura()
        }
        btnSolicPendentes.setOnClickListener {
            trocarParaTelaSolicPendentes()
        }
    }

    private fun trocarParaTelaEmprestimoDeLivros() {
        val intent = Intent(requireContext(), TelaEmprestLivrosUsu::class.java)
        startActivity(intent)
    }

    private fun trocarParaTelaMeusLivros() {
        val intent = Intent(requireContext(), TelaMeusLivrosUsu::class.java)
        startActivity(intent)
    }

    private fun trocarParaTelaHistoricoLeitura() {
        val intent = Intent(requireContext(), TelaHistoricoUsu::class.java)
        startActivity(intent)
    }

    private fun trocarParaTelaSolicPendentes() {
        val intent = Intent(requireContext(), TelaSolicitaPendUsu::class.java)
        startActivity(intent)
    }
}

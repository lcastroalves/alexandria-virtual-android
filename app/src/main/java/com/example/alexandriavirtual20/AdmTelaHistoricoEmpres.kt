package com.example.alexandriavirtual20

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.Solicitacao
import com.example.alexandriavirtual20.adapter.SolicitacaoAdapter

class AdmTelaHistEmprestimos : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.adm_tela_historico_empres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerLivrosRetirados)
        val tabPendentes = view.findViewById<TextView>(R.id.tabPendentes)
        val tabUsuarios = view.findViewById<TextView>(R.id.tabUsuarios)


        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        tabPendentes.setOnClickListener {
            Toast.makeText(requireContext(), "Ir para Pendentes", Toast.LENGTH_SHORT).show()
        }

        tabUsuarios.setOnClickListener {
            Toast.makeText(requireContext(), "Ir para Usuários", Toast.LENGTH_SHORT).show()
        }


        val livros = listOf(
            Solicitacao(
                "Ciência da Computação", "Ednaldo Rosa",
                "Felipe Barroso", "felipebvm@gmail.com",
                "09 de Set", "12 de Set", "Balcão da Biblioteca",
                R.drawable.livro1
            ),
            Solicitacao(
                "Java como Programar", "Deitel",
                "Felipe Barroso", "felipebvm@gmail.com",
                "03 de Out", "08 de Out", "Balcão da Biblioteca",
                R.drawable.livro2
            )
        )

        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = SolicitacaoAdapter(
            livros,
            onAutorizar = { livro ->
                Toast.makeText(requireContext(), "Livro devolvido: ${livro.titulo}", Toast.LENGTH_SHORT).show()
            },
            onRecusar = { livro ->
                Toast.makeText(requireContext(), "Livro atrasado: ${livro.titulo}", Toast.LENGTH_SHORT).show()
            }
        )


        PagerSnapHelper().attachToRecyclerView(recycler)
    }
}
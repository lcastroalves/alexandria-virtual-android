package com.example.alexandriavirtual20

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.Solicitacao
import com.example.alexandriavirtual20.adapter.SolicitacaoAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdmTelaHistoricoEmpres.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdmTelaHistoricoEmpres : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.adm_tela_historico_empres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerLivrosRetirados)
        val tabPendentes = view.findViewById<TextView>(R.id.tabPendentes)
        val tabUsuarios = view.findViewById<TextView>(R.id.tabUsuarios)

        tabPendentes.setOnClickListener {
            (activity as? AdmAMain)?.replaceFragment(AdmTelaSolicPend())
        }
        tabUsuarios.setOnClickListener {
            (activity as? AdmAMain)?.replaceFragment(AdmTelaHistoricoEmpres())
        }

        val livros = listOf(
            Solicitacao(
                "Ciência da Computação",
                "Ednaldo Rosa",
                "Felipe Barroso",
                "felipebvm@gmail.com",
                "09 de Set",
                "12 de Set",
                "Balcão da Biblioteca",
                R.drawable.livro1
            ),
            Solicitacao( "Java como Programar", "Deitel", "Felipe Barroso", "felipebvm@gmail.com", "03 de Out", "08 de Out", "Balcão da Biblioteca", R.drawable.livro2 )
        )
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = SolicitacaoAdapter(
            livros,
            onAutorizar = { livro ->
                Toast.makeText(
                    requireContext(),
                    "Livro devolvido: ${livro.titulo}",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onRecusar = { livro ->
                Toast.makeText(
                    requireContext(),
                    "Livro atrasado: ${livro.titulo}",
                    Toast.LENGTH_SHORT
                ).show()
            })

        PagerSnapHelper().attachToRecyclerView(recycler)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdmTelaHistoricoEmpres.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdmTelaHistoricoEmpres().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
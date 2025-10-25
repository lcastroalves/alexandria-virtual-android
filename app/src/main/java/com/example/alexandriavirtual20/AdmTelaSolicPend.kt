package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
 * Use the [AdmTelaSolicPend.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdmTelaSolicPend : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        class TelaSolicitacoesPendentes : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                enableEdgeToEdge()
                setContentView(R.layout.adm_tela_solic_pend)

                val btnBack = findViewById<ImageButton>(R.id.btnBack)
                val recycler = findViewById<RecyclerView>(R.id.recyclerSolicitacoes)
                val btnPendentes = findViewById<TextView>(R.id.tabPendentes)
                val btnUsuarios = findViewById<TextView>(R.id.tabUsuarios)

                btnPendentes.setOnClickListener {
                    val intencao = Intent(this, AdmTelaUsuCadast::class.java)
                    startActivity(intencao)
                }

                btnBack.setOnClickListener { finish() }

                val solicitacoes = listOf(
                    Solicitacao(
                        "Ciências da Computação", "Ername Rosa Martins",
                        "Felipe Barroso", "felipebvm@gmail.com",
                        "09 de Set", "12 de Set", "Balcão da Biblioteca",
                        R.drawable.livro1
                    ),
                    Solicitacao(
                        "Java Avançado", "Paul J. Deitel",
                        "Lucas Silva", "lucas@gmail.com",
                        "15 de Set", "18 de Set", "Balcão da Biblioteca",
                        R.drawable.livro2
                    )
                )

                recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                recycler.adapter = SolicitacaoAdapter(
                    solicitacoes,
                    onAutorizar = { solicitacao ->
                        Toast.makeText(
                            this,
                            "Retirada autorizada para ${solicitacao.usuario}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onRecusar = { solicitacao ->
                        Toast.makeText(
                            this,
                            "Solicitação recusada de ${solicitacao.usuario}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                // Faz parar um item por vez (scroll estilo “página”)
                PagerSnapHelper().attachToRecyclerView(recycler)
            }
        }
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
        return inflater.inflate(R.layout.adm_tela_solic_pend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        Programa aqui dentro

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
            AdmTelaSolicPend().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
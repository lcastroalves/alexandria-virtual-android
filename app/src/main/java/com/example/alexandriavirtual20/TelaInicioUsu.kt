package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.AtividadeAdapter
import com.example.alexandriavirtual20.adapter.AtividadeAdapterTelaInicio
import com.example.alexandriavirtual20.adapter.EventoAdapterTelaInicio
import com.example.alexandriavirtual20.adapter.LivroAdapterSoCapa
import com.example.alexandriavirtual20.adapter.ProdutoAdapter
import com.example.alexandriavirtual20.model.Atividade
import com.example.alexandriavirtual20.model.Evento
import com.example.alexandriavirtual20.model.Livro

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TelaInicioUsu.newInstance] factory method to
 * create an instance of this fragment.
 */
class TelaInicioUsu : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var recyclerViewLivros: RecyclerView
    private lateinit var recyclerViewEventos: RecyclerView
    private lateinit var recyclerViewAtividades: RecyclerView
    private lateinit var btnVerMaisEventos : TextView
    private lateinit var btnVerMaisAtividades: TextView
    private lateinit var btnSinoNotifi : ImageButton
    private lateinit var btnLevBib : Button
    private lateinit var btnCap : Button
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
        return inflater.inflate(R.layout.tela_inicio_usu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewLivros = view.findViewById(R.id.recyclerViewLivros)
        recyclerViewEventos = view.findViewById(R.id.recyclerViewEventos)
        recyclerViewAtividades = view.findViewById(R.id.recyclerViewAtividades)
        btnVerMaisEventos = view.findViewById(R.id.btnVerMaisEventos)
        btnVerMaisAtividades = view.findViewById(R.id.btnVerMaisAtividades)
        btnSinoNotifi = view.findViewById(R.id.btnSinoNotif)
        btnLevBib = view.findViewById(R.id.btnLevBib)
        btnCap = view.findViewById(R.id.btnCap)

        val livros = mutableListOf(
            Livro("Ciência da computação", "","Ernane Rosa Martins", R.drawable.livro1, "4.5"),
            Livro("Ciência da computação", "","Ernane Rosa Martins", R.drawable.livro2, "4.0"),
            Livro("Ciência da computação","", "Ernane Rosa Martins", R.drawable.livro3, "4.3"),
            Livro("Ciência da computação", "","Ernane Rosa Martins", R.drawable.livro4, "4.6")
        )

        val eventos = mutableListOf(
            Evento(imagem = R.drawable.totoro, nome = "O meu Amigo Totoro", data = "16 Set", horario = "16:00 - 17:30", descricao = "xfg", breveDescricao = "fsdfg", local = "Auditório Principal"),
            Evento(imagem = R.drawable.livro1, nome = "Feira Literária Alexandria", data = "22 Set", horario = "09:00 - 18:00", descricao = "fdgs", breveDescricao = "sdfg", local = "Pátio Cultural Alexandria"),
            Evento(imagem = R.drawable.livro2, nome = "Workshop de Programação Kotlin", data = "28 Set", horario = "14:00 - 17:00", descricao = "dfg", breveDescricao = "dgfdf", local = "Sala de Informática 2"),
            Evento(imagem = R.drawable.livro3, nome = "Clube do Livro - Ficção Científica", data = "03 Out", horario = "19:00 - 21:00", descricao = "sdf", breveDescricao = "sdfsd", local = "Biblioteca Central"),
            Evento(imagem = R.drawable.livro4, nome = "Sarau Poético Noturno", data = "10 Out", horario = "20:00 - 22:30", descricao = "sdfs", breveDescricao = "sdfs", local = "Praça das Artes")
        )

        val atividades = mutableListOf(
            Atividade("Sala de Jogos", "", "","","",R.drawable.jogos),
            Atividade("Cinema", "", "","","",R.drawable.cinema)
        )

        // adapters
        val adapterLivro = LivroAdapterSoCapa(livros){ livro ->
            val intent = Intent(requireContext(), TelaInfoLivroUsu::class.java)
            startActivity(intent)
            // Somente coisas que dizem respeito ao item individual
        }

        val adapterEvento = EventoAdapterTelaInicio(eventos){ evento ->
            val intent = Intent(requireContext(), TelaInfoEventoUsu::class.java)
            startActivity(intent)
        }

        val adapterAtividade = AtividadeAdapterTelaInicio(atividades){ atividade ->
            val intent = Intent(requireContext(), TelaInfoAtividadeUsu::class.java)
            startActivity(intent)
        }

        // orientacoes dos recycler views
        recyclerViewLivros.layoutManager = LinearLayoutManager(

            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        recyclerViewEventos.layoutManager = LinearLayoutManager(

            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        recyclerViewAtividades.layoutManager = LinearLayoutManager(

            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // chamadas
        recyclerViewLivros.adapter = adapterLivro
        recyclerViewEventos.adapter = adapterEvento
        recyclerViewAtividades.adapter = adapterAtividade

        btnVerMaisEventos.setOnClickListener {
            val intent = Intent(requireContext(), TelaEventosFuturUsu::class.java)
            startActivity(intent)
        }

        btnVerMaisAtividades.setOnClickListener {
            val intent = Intent(requireContext(), TelaAtividadesUsu::class.java)
            startActivity(intent)
        }
        btnSinoNotifi.setOnClickListener {
            val intent = Intent (requireContext(), TelaNotificacoesUsu::class.java)
            startActivity(intent)
        }
        btnLevBib.setOnClickListener {
            val intent = Intent(requireContext(), TelaLevantBibliogUsu::class.java)
            startActivity(intent)
        }
        btnCap.setOnClickListener {
            val intent = Intent(requireContext(), TelaCapacitacoesUsu::class.java)
            startActivity(intent)
        }
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
            TelaInicioUsu().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
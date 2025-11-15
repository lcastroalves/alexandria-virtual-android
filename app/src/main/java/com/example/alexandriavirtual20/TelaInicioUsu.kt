package com.example.alexandriavirtual20

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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
import com.example.alexandriavirtual20.model.Produto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


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
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var fireBase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        fbAuth = FirebaseAuth.getInstance()
        fireBase = FirebaseFirestore.getInstance()


        carregarLivros()
        carregarEventos()
        carregarAtividades()


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

    private fun carregarLivros(){

        val produtos = mutableListOf<Produto>()

        fireBase.collection("produtos").get().addOnSuccessListener { query ->

            for (doc in query.documents){
                val titulo = doc.getString("titulo") ?: ""
                val autor = doc.getString("autor") ?: ""
                val imagemBase64 = doc.getString("imagem") ?: ""

                produtos.add(Produto(titulo, autor, imagemBase64, false))
            }

            val adapterLivro = LivroAdapterSoCapa(produtos){ livro ->
                val intent = Intent(requireContext(), TelaInfoLivroUsu::class.java)
                startActivity(intent)
                // Somente coisas que dizem respeito ao item individual
            }

            recyclerViewLivros.layoutManager = LinearLayoutManager(

                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            recyclerViewLivros.adapter = adapterLivro
        }
    }

    private fun carregarEventos(){
        val eventos = mutableListOf<Evento>()

        fireBase.collection("evento").get().addOnSuccessListener { query ->

            for(doc in query.documents){
                val nomeEvento = doc.getString("nome") ?: ""
                val horaEvento = doc.getString("horario")?: ""
                val localEvento = doc.getString("local")?: ""
                val dataEvento = doc.getString("data")?: ""
                val capaEvento = doc.getString("imagem")?: ""

                eventos.add(Evento(capaEvento,nomeEvento,dataEvento,horaEvento,"","",localEvento))
            }

            val adapterEvento = EventoAdapterTelaInicio(eventos){ evento ->
                val intent = Intent(requireContext(), TelaInfoEventoUsu::class.java)
                startActivity(intent)
            }

            recyclerViewEventos.layoutManager = LinearLayoutManager(

                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            recyclerViewEventos.adapter = adapterEvento
        }
    }

    private fun carregarAtividades(){
        val atividades = mutableListOf<Atividade>()

        fireBase.collection("atividade").get().addOnSuccessListener { query ->

            for(doc in query.documents){
                val nomeAtividade = doc.getString("nome") ?: ""
                val horaAtividade = doc.getString("horario")?: ""
                val localAtividade = doc.getString("local")?: ""
                val descricaoAtividade = doc.getString("descricao")?: ""
                val breveDescAtividade = doc.getString("breveDescricao")?: ""
                val capaAtividade = doc.getString("imagem")?: ""

                atividades.add(Atividade(nomeAtividade, horaAtividade, localAtividade, descricaoAtividade, breveDescAtividade,capaAtividade))
            }

            val adapterAtividade = AtividadeAdapterTelaInicio(atividades){ atividade ->
                val intent = Intent(requireContext(), TelaInfoAtividadeUsu::class.java)
                startActivity(intent)
            }

            recyclerViewAtividades.layoutManager = LinearLayoutManager(

                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )

            recyclerViewAtividades.adapter = adapterAtividade
        }
    }
}
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
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration


class TelaInicioUsu : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var recyclerViewLivros: RecyclerView
    private lateinit var recyclerViewEventos: RecyclerView
    private lateinit var recyclerViewAtividades: RecyclerView
    private lateinit var btnVerMaisEventos : TextView
    private lateinit var btnVerMaisAtividades: TextView
    private lateinit var btnSinoNotifi : ImageButton
    private lateinit var existeNotificacao : TextView
    private lateinit var btnLevBib : Button
    private lateinit var btnCap : Button
    private lateinit var searchView: SearchView
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var fireBase: FirebaseFirestore
    private var notificacaoListener: ListenerRegistration? = null

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
        existeNotificacao = view.findViewById(R.id.qntNotific)
        btnLevBib = view.findViewById(R.id.btnLevBib)
        btnCap = view.findViewById(R.id.btnCap)
        searchView = view.findViewById(R.id.searchView)

        fbAuth = FirebaseAuth.getInstance()
        fireBase = FirebaseFirestore.getInstance()

        carregarLivros()
        carregarEventos()
        carregarAtividades()
        exibirQtdNotificacoes()


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

    private fun exibirQtdNotificacoes() {

        val usuarioId = fbAuth.currentUser?.uid ?: return

        notificacaoListener = fireBase.collection("usuario")
            .document(usuarioId)
            .collection("notificacoes")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    existeNotificacao.visibility = View.GONE
                    return@addSnapshotListener
                }

                val qtd = snapshot?.size() ?: 0

                if (qtd > 0) {
                    existeNotificacao.text = qtd.toString()
                    existeNotificacao.visibility = View.VISIBLE
                } else {
                    existeNotificacao.visibility = View.GONE
                }
            }
    }


    private fun carregarLivros(){

        val produtos = mutableListOf<Livro>()

        fireBase.collection("livros").get().addOnSuccessListener { query ->

            for (doc in query.documents){

                val id = doc.id
                val titulo = doc.getString("titulo") ?: ""
                val autor = doc.getString("autor") ?: ""
                val imagemBase64 = doc.getString("capa") ?: ""

                produtos.add(
                    Livro(
                        id = id,
                        titulo = titulo,
                        autor = autor,
                        capa = imagemBase64
                    )
                )
            }

            val adapterLivro = LivroAdapterSoCapa(produtos){ livro ->

                val intent = Intent(requireContext(), TelaInfoLivroUsu::class.java)
                intent.putExtra("livroId", livro.id)
                startActivity(intent)

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
                intent.putExtra("nomeEvento", evento.nome)
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

        val atividades = mutableListOf(
            Atividade("Cinema", "9 - 22", "Auditório Principal", "A nossa biblioteca oferece um espaço para reprodução de filmes, diversão garantida para toda família!", "A nossa biblioteca oferece um espaço para reprodução de filmes, diversão garantida para toda família!",R.drawable.cinema),
            Atividade("Sala de Jogos", "9 - 22", "Sala 2", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21",R.drawable.jogos),
            Atividade("Clube do Livro", "9 - 22", "Auditório Principal", "O filme de animação será reproduzido no auditório das 16 - 18", "O filme de animação será reproduzido no auditório das 16 - 18",R.drawable.iconelivro)
        )

            val adapterAtividade = AtividadeAdapterTelaInicio(atividades){ atividade ->
                val intent = Intent(requireContext(), TelaInfoAtividadeUsu::class.java)
                intent.putExtra("nomeAtividade", atividade.nome)
                intent.putExtra("horarioAtividade", atividade.horario)
                intent.putExtra("imagemAtividade", atividade.imagem)
                intent.putExtra("descricaoAtividade", atividade.descricao)
                intent.putExtra("localAtividade", atividade.local)
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
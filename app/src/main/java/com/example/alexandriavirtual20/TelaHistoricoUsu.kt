package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroAdapterHistorico
import com.example.alexandriavirtual20.model.Livro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TelaHistoricoUsu : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: LivroAdapterHistorico

    private val listaOriginal = mutableListOf<Livro>()
    private val listaFiltro = mutableListOf<Livro>()

    private lateinit var fireBase : FirebaseFirestore
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_historico_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        searchView = findViewById(R.id.searchViewEventos)
        recyclerView = findViewById(R.id.recyclerView)

        fireBase = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapter = LivroAdapterHistorico(
            listaFiltro,
            onInfoClick = { livro ->
                val intent = Intent(this, TelaInfoLivroUsu::class.java)
                intent.putExtra("livroId", livro.id)
                startActivity(intent)
            },
            onReviewClick = { livro ->
                val intent = Intent(this, TelaReviewUsu::class.java)
                intent.putExtra("livroId", livro.id)
                startActivity(intent)
            },
            onFavotiroClick = { livro ->
                salvarOuRemoverFavorito(livro)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        carregarHistorico()
        configurarPesquisa()
    }


    private fun carregarHistorico(){
        val usuarioID = fbAuth.currentUser?.uid

        if(usuarioID != null){

            fireBase.collection("usuario")
                .document(usuarioID)
                .collection("historico")
                .orderBy("dataVisualizacao", Query.Direction.DESCENDING)
                .limit(10).get()
                .addOnSuccessListener { doc ->

                val listaIds = doc.documents.mapNotNull {
                    it.getString("id")
                }

                if (listaIds.isEmpty()) {
                    listaOriginal.clear()
                    listaFiltro.clear()
                    adapter.notifyDataSetChanged()
                    return@addOnSuccessListener
                }

                buscarLivrosDoHistorico(listaIds)
            }
        }
    }

    private fun buscarLivrosDoHistorico(ids: List<String>) {
        val usuarioID = fbAuth.currentUser?.uid ?: return

        listaOriginal.clear()
        listaFiltro.clear()
        adapter.notifyDataSetChanged()

        // Primeiro: pega a lista de favoritos do usuário
        fireBase.collection("usuario").document(usuarioID).collection("favoritos").get()
            .addOnSuccessListener { favDocs ->

                val favoritosIds = favDocs.documents.map { it.id }.toSet()

                // Agora pega cada livro do histórico
                for (id in ids) {
                    fireBase.collection("livros").document(id).get()
                        .addOnSuccessListener { doc ->

                            if (!doc.exists()) return@addOnSuccessListener

                            val livro = doc.toObject(Livro::class.java)
                            livro?.id = doc.id

                            if (livro != null) {
                                // marca como favorito se estiver na coleção favoritos
                                livro.favorito = favoritosIds.contains(livro.id)

                                // pegar avaliações
                                val mediaAvaliacao = doc.getDouble("mediaAvaliacao") ?: 0.0
                                val totalAvaliacoes = doc.getLong("totalAvaliacoes") ?: 0

                                livro.mediaAvaliacao = mediaAvaliacao
                                livro.totalAvaliacoes = totalAvaliacoes
                                livro.avaliacoes = totalAvaliacoes.toInt()

                                listaOriginal.add(livro)
                                listaFiltro.add(livro)
                            }

                            if (listaFiltro.size == ids.size) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                }
            }
    }


    private fun configurarPesquisa() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {

                val texto = newText?.lowercase()?.trim().orEmpty()

                listaFiltro.clear()

                if (texto.isEmpty()) {
                    listaFiltro.addAll(listaOriginal)
                } else {
                    listaFiltro.addAll(
                        listaOriginal.filter { livro ->

                            (livro.titulo ?: "").lowercase().contains(texto) || (livro.autor ?: "").lowercase().contains(texto)

                        }
                    )
                }

                adapter.notifyDataSetChanged()
                return true
            }
        })
    }

    private fun salvarOuRemoverFavorito(livro: Livro) {

        val usuarioID = fbAuth.currentUser?.uid ?: return

        val favRef = fireBase.collection("usuario").document(usuarioID).collection("favoritos").document(livro.id)

        if (livro.favorito) {
            val dados = hashMapOf(
                "id" to livro.id,
                "data" to System.currentTimeMillis()
            )
            favRef.set(dados)
        } else {
            favRef.delete()
        }
    }
}

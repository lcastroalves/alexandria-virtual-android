package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
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
        searchView = findViewById(R.id.searchViewLivros)
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
                Toast.makeText(this, "Favorito = ${livro.favorito}", Toast.LENGTH_SHORT).show()
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
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar histórico.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarLivrosDoHistorico(ids: List<String>) {
        listaOriginal.clear()
        listaFiltro.clear()
        adapter.notifyDataSetChanged()

        for (id in ids) {
            fireBase.collection("livros").document(id).get()
                .addOnSuccessListener { doc ->
                    val livro = doc.toObject(Livro::class.java)

                    if (livro != null) {
                        listaOriginal.add(livro)
                        listaFiltro.add(livro)
                    }

                    // Atualiza SOMENTE quando todos forem carregados
                    if (listaFiltro.size == ids.size) {
                        adapter.notifyDataSetChanged()
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
                            livro.titulo.lowercase().contains(texto) ||
                                    livro.autor.lowercase().contains(texto)
                        }
                    )
                }

                adapter.notifyDataSetChanged()
                return true
            }
        })
    }
}

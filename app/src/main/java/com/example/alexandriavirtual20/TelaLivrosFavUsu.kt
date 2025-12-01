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
import com.example.alexandriavirtual20.adapter.LivroAdapterFavoritos
import com.example.alexandriavirtual20.model.Livro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaLivrosFavUsu : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterLivro: LivroAdapterFavoritos

    private val listaOriginal = mutableListOf<Livro>()
    private val listaFiltrada = mutableListOf<Livro>()

    private lateinit var fireBase : FirebaseFirestore
    private lateinit var fbAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_livros_fav_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        searchView = findViewById(R.id.searchViewEventos)
        recyclerView = findViewById(R.id.recyclerView)

        fireBase = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapterLivro = LivroAdapterFavoritos(
            listaFiltrada,
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
            onFavoritoClick = { livro ->

                if (!livro.favorito) {
                    removerFavorito(livro)
                }


            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterLivro

        carregarFavoritos()
        configurarPesquisa()
    }

    private fun carregarFavoritos() {

        val userId = fbAuth.currentUser?.uid ?: return

        fireBase.collection("usuario").document(userId).collection("favoritos").get().addOnSuccessListener { docs ->

                val LivrosFavIds = docs.documents.mapNotNull { it.getString("id") }

                if (LivrosFavIds.isEmpty()) {
                    listaOriginal.clear()
                    listaFiltrada.clear()
                    adapterLivro.notifyDataSetChanged()
                    return@addOnSuccessListener
                }

                buscarLivrosFavoritos(LivrosFavIds)
            }
            .addOnFailureListener {
                print("Erro ao carregar favoritos.")
            }
    }

    private fun buscarLivrosFavoritos(LivrosFavIds: List<String>) {

        listaOriginal.clear()
        listaFiltrada.clear()
        adapterLivro.notifyDataSetChanged()

        for (id in LivrosFavIds) {
            fireBase.collection("livros").document(id).get().addOnSuccessListener { doc ->

                val livro = doc.toObject(Livro::class.java)
                    livro?.id = doc.id
                    livro?.favorito = true   // marca como favorito

                    if (livro != null) {

                        // pegar avaliações
                        val mediaAvaliacao = doc.getDouble("mediaAvaliacao") ?: 0.0
                        val totalAvaliacoes = doc.getLong("totalAvaliacoes") ?: 0

                        // arredondar para 1 casa
                        val mediaArredondada =
                            String.format("%.1f", mediaAvaliacao).replace(",", ".").toDouble()

                        livro.mediaAvaliacao = mediaAvaliacao
                        livro.totalAvaliacoes = totalAvaliacoes
                        livro.avaliacoes = totalAvaliacoes.toInt()

                        listaOriginal.add(livro)
                        listaFiltrada.add(livro)
                    }

                    if (listaFiltrada.size == LivrosFavIds.size) {
                        adapterLivro.notifyDataSetChanged()
                    }
                }
        }
    }

    private fun removerFavorito(livro: Livro) {
        val usuarioID = fbAuth.currentUser?.uid ?: return

        // Remove do Firestore
        fireBase.collection("usuario").document(usuarioID).collection("favoritos").document(livro.id).delete().addOnSuccessListener {

                // Remove da lista
                listaOriginal.remove(livro)
                listaFiltrada.remove(livro)

                adapterLivro.notifyDataSetChanged()
            }
    }

    private fun configurarPesquisa() {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {

                val texto = newText?.trim()?.lowercase().orEmpty()

                listaFiltrada.clear()

                if (texto.isEmpty()) {
                    // mostra toda lista
                    listaFiltrada.addAll(listaOriginal)
                } else {
                    // filtra por título ou autor
                    listaFiltrada.addAll(
                        listaOriginal.filter { livro ->
                            livro.titulo.lowercase().contains(texto) ||
                                    livro.autor.lowercase().contains(texto)
                        }
                    )
                }

                adapterLivro.notifyDataSetChanged()
                return true
            }
        })
    }
}

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

class TelaLivrosFavUsu : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterLivro: LivroAdapterFavoritos

    private val listaOriginal = mutableListOf<Livro>()
    private val listaFiltrada = mutableListOf<Livro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_livros_fav_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        searchView = findViewById(R.id.searchViewLivros)
        recyclerView = findViewById(R.id.recyclerView)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // ----------------------------
        // LISTA INICIAL DE LIVROS
        // ----------------------------
        listaOriginal.addAll(
            listOf(
                Livro("111111111", "Ciências da Computação", "Ernanne Rosa Martins", "Ernanne Rosa Martins", R.drawable.livro1, "130"),
                Livro("222222222", "Ciências da Computação", "Ernanne Rosa Martins", "Ernanne Rosa Martins", R.drawable.livro1, "130"),
                Livro("33333333", "Java como Programar", "Ernanne Rosa Martins", "Ernanne Rosa Martins", R.drawable.livro2, "230"),
                Livro("44444444", "Java Avançado", "Ernanne Rosa Martins", "Ernanne Rosa Martins", R.drawable.livro3, "150"),
                Livro("555555555", "Redes de Computadores", "Tanenbaum & Wetherall", "Tanenbaum & Wetherall", R.drawable.livro4, "170"),
            )
        )

        // lista filtrada começa igual
        listaFiltrada.addAll(listaOriginal)

        // ----------------------------
        // ADAPTER
        // ----------------------------
        adapterLivro = LivroAdapterFavoritos(
            listaFiltrada,
            onInfoClick = { livro ->
                val intent = Intent(this, TelaInfoLivroUsu::class.java)
                intent.putExtra("livro", livro)
                startActivity(intent)
            },
            onReviewClick = { livro ->
                val intent = Intent(this, TelaReviewUsu::class.java)
                intent.putExtra("livro", livro)
                startActivity(intent)
            },
            onFavoritoClick = { livro ->
                Toast.makeText(this, "Favorito = ${livro.favorito}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterLivro

        configurarPesquisa()
    }

    // --------------------------------------------------
    // 🔍 FILTRO DA BARRA DE PESQUISA
    // --------------------------------------------------
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

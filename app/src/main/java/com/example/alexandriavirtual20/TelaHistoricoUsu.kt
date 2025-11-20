package com.example.alexandriavirtual20

import     android.content.Intent
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

class TelaHistoricoUsu : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: LivroAdapterHistorico

    private val listaOriginal = mutableListOf<Livro>()
    private val listaFiltro = mutableListOf<Livro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_historico_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        searchView = findViewById(R.id.searchViewLivros)
        recyclerView = findViewById(R.id.recyclerView)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // --------------------------------------------------------------------
        // ⚠️ VERIFIQUE SE SUA CLASSE Livro TEM ESTES CAMPOS:
        // titulo, autor, imagem, favorito, etc…
        // E SE IMPLEMENTA Serializable OU Parcelable.
        // --------------------------------------------------------------------

        listaOriginal.addAll(
            listOf(
                Livro("111111111", "Ciências da Computação", "Ernanne Rosa Martins", "Ernanne Rosa Martins", "", "130")
            )
        )

        listaFiltro.addAll(listaOriginal)

        adapter = LivroAdapterHistorico(
            listaFiltro,
            onInfoClick = { livro ->
                val intent = Intent(this, TelaInfoLivroUsu::class.java)
                intent.putExtra("livro", livro) // PRECISA SER Serializable/Parcelable
                startActivity(intent)
            },
            onReviewClick = { livro ->
                val intent = Intent(this, TelaReviewUsu::class.java)
                intent.putExtra("livro", livro)
                startActivity(intent)
            },
            onFavotiroClick = { livro ->
                Toast.makeText(this, "Favorito = ${livro.favorito}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        configurarPesquisa()
    }

    // --------------------------------------------------------------------
    // 🔍 FILTRO DA BARRA DE PESQUISA
    // --------------------------------------------------------------------
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

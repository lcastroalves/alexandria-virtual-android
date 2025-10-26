package com.example.alexandriavirtual20

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroAdapterFavoritos
import com.example.alexandriavirtual20.adapter.LivroAdapterHistorico
import com.example.alexandriavirtual20.model.Livro


class TelaLivrosFavUsu : AppCompatActivity() {

    private lateinit var btnVoltar : ImageButton
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_livros_fav_usu)

        btnVoltar= findViewById(R.id.botaoVoltar)
        searchView = findViewById(R.id.searchViewLivros)
        recyclerView = findViewById(R.id.recyclerView)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val livros = mutableListOf(
            Livro("Ciencia da computação","e tecnologias digitais","Ernanne Rosa Martins",R.drawable.livro," (30 avaliações)",true),
            Livro("Ciencia da computação","e tecnologias digitais","Ernanne Rosa Martins",R.drawable.livro2," (10 avaliações)",true),
            Livro("Ciencia da computação","e tecnologias digitais","Ernanne Rosa Martins",R.drawable.livro1," (60 avaliações)",true),
            Livro("Ciencia da computação","e tecnologias digitais","Ernanne Rosa Martins",R.drawable.livro3," (100 avaliações)",true),
            Livro("Ciencia da computação","e tecnologias digitais","Lupi Barroso Bvm",R.drawable.livro4," (1000 avaliações)",true)
        )

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Quando o usuário pressiona ENTER / Buscar
                if (!query.isNullOrEmpty()) {
                    Toast.makeText(this@TelaLivrosFavUsu, "Buscando: $query", Toast.LENGTH_SHORT).show()
                    // Aqui você pode filtrar sua lista de livros
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Quando o texto muda (digitação em tempo real)
                if (!newText.isNullOrEmpty()) {
                    // Filtra enquanto o usuário digita
                }
                return true
            }
        })

        val adapterLivro = LivroAdapterFavoritos(
            livros,
            onInfoClick = { livro ->
                val intent = Intent(this, TelaInfoLivroUsu::class.java)
                startActivity(intent)
            },
            onReviewClick = { livro ->
                val intent = Intent(this, TelaReviewUsu::class.java)
                startActivity(intent)
            },
            onFavotiroClick = { livro ->
                Toast.makeText(this,"Favorito = ${livro.favorito}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterLivro

    }
}




package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaHistoricoUsu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_historico_usu)

        val btnback: ImageButton = findViewById(R.id.botaoVoltar3)
        lateinit var searchView: SearchView
        searchView = findViewById(R.id.searchViewLivros)
        val btnFav1: ImageView = findViewById(R.id.btnFav1)
        val btnFav2: ImageView = findViewById(R.id.btnFav2)
        var isFavorited = false

        btnback.setOnClickListener {
            var intencao = Intent(this, TelaMenuEmprestUsu::class.java)
            startActivity(intencao)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Quando o usuário pressiona ENTER / Buscar
                if (!query.isNullOrEmpty()) {
                    Toast.makeText(this@TelaHistoricoUsu, "Buscando: $query", Toast.LENGTH_SHORT).show()
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
        btnFav1.setOnClickListener {
            if (isFavorited) {
                btnFav1.setImageResource(R.drawable.coracao_vazio) // muda para vazio
            } else {
                btnFav1.setImageResource(R.drawable.coracao_cheio) // volta a ficar cheio
            }
            isFavorited = !isFavorited
        }
        btnFav2.setOnClickListener {
            if (isFavorited) {
                btnFav2.setImageResource(R.drawable.coracao_vazio) // muda para vazio
            } else {
                btnFav2.setImageResource(R.drawable.coracao_cheio) // volta a ficar cheio
            }
            isFavorited = !isFavorited
        }
    }
}
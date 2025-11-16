package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroRetiradaAdapter
import com.example.alexandriavirtual20.model.Livro

class TelaRetirarLivroUsu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_retirar_livro_usu)

        val btnBack: ImageButton = findViewById(R.id.btnback)
        val btnConfirmar: Button = findViewById(R.id.btnConfirmar)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerLivrosSelecionados)
        val txtStatusLivro: TextView = findViewById(R.id.textView7)

        val livrosSelecionados = intent.getParcelableArrayListExtra<Livro>("livrosSelecionados")
        var livroParaExibir: Livro? = null

        if (livrosSelecionados.isNullOrEmpty()) {

            txtStatusLivro.text = "Nenhum livro selecionado."
            Toast.makeText(this, "Nenhum livro recebido.", Toast.LENGTH_SHORT).show()

        } else {

            livroParaExibir = livrosSelecionados.first()

            txtStatusLivro.text =
                "Solicitação em andamento para ${livroParaExibir.titulo}"


            val nomes = livrosSelecionados.joinToString(", ") { it.titulo }
            Toast.makeText(this, "Livros recebidos: $nomes", Toast.LENGTH_LONG).show()

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = LivroRetiradaAdapter(livrosSelecionados)
        }


        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        btnConfirmar.setOnClickListener {

            if (livroParaExibir == null) {
                Toast.makeText(this, "Erro: Nenhum livro válido.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Livro(s) solicitado(s) com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}

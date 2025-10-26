package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.round

class TelaReviewUsu : AppCompatActivity() {
    private lateinit var estrelas: List<ImageView>
    private lateinit var editAvaliacao: EditText
    private lateinit var btnEnviar: Button
    private lateinit var txtNotaGeral: TextView
    private var notaSelecionada = 0
    private val avaliacoes = mutableListOf(4.0, 5.0, 3.5, 4.5)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_review_usu)

        val recyclerLivros = findViewById<RecyclerView>(R.id.recyclerLivros)
        val livros = listOf(
            LivroReview("Ciências da Computação", "Ername Rosa Martins", R.drawable.livro1),
            LivroReview("Java Avançado", "Paul J. Deitel", R.drawable.livro2),
            LivroReview("Java como Programar", "Paul J. Deitel", R.drawable.livro3),
            LivroReview("Redes de Computadores", "Tanembaum e Wetherall", R.drawable.livro4)
        )

        recyclerLivros.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerLivros.adapter = LivroReviewAdapter(livros)
        PagerSnapHelper().attachToRecyclerView(recyclerLivros)

        val btnBack : ImageButton = findViewById(R.id.btnback)
        editAvaliacao = findViewById(R.id.editAvaliacao)
        btnEnviar = findViewById(R.id.btnEnviar)
        txtNotaGeral = findViewById(R.id.txtNotaGeral)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        estrelas = listOf(
            findViewById(R.id.estrela1),
            findViewById(R.id.estrela2),
            findViewById(R.id.estrela3),
            findViewById(R.id.estrela4),
            findViewById(R.id.estrela5)
        )
        // Mostra a média inicial
        atualizarNotaGeral()

        // Clique nas estrelas
        estrelas.forEachIndexed { index, estrela ->
            estrela.setOnClickListener {
                notaSelecionada = index + 1
                atualizarEstrelas(notaSelecionada)
            }
        }
        btnEnviar.setOnClickListener {
            val texto = editAvaliacao.text.toString()

            if (notaSelecionada == 0 || texto.isBlank()) {
                Toast.makeText(this, "Dê uma nota e escreva sua avaliação!", Toast.LENGTH_SHORT).show()
            } else {
                // Adiciona nova nota
                avaliacoes.add(notaSelecionada.toDouble())
                atualizarNotaGeral()

                Toast.makeText(
                    this,
                    "Livro Avaliado com Sucesso!\nNota: $notaSelecionada\nTexto: $texto",
                    Toast.LENGTH_LONG
                ).show()

                // Reseta os campos
                editAvaliacao.text.clear()
                atualizarEstrelas(0)
                notaSelecionada = 0
            }
        }
    }
    private fun atualizarEstrelas(nota: Int) {
        estrelas.forEachIndexed { index, estrela ->
            if (index < nota) {
                estrela.setImageResource(R.drawable.ic_star_full)
            } else {
                estrela.setImageResource(R.drawable.ic_star_border)
            }
        }
    }

    private fun atualizarNotaGeral() {
        val media = if (avaliacoes.isNotEmpty()) avaliacoes.average() else 0.0
        val mediaArredondada = round(media * 10) / 10 // ex: 4.333 -> 4.3
        txtNotaGeral.text = "$mediaArredondada ★★★★★"
    }
}




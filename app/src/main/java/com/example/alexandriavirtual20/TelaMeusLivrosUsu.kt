package com.example.alexandriavirtual20

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.collections.toMutableList
import kotlin.jvm.java

class TelaMeusLivrosUsu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_meus_livros_usu)

        val btnBack: ImageButton = findViewById(R.id.btnback)
        val btnAvaliarLivros: Button = findViewById(R.id.btnAvaliarLivros)
        val livro1: ImageView = findViewById(R.id.livro1)
        val livro2: ImageView = findViewById(R.id.livro2)
        val livro3: ImageView = findViewById(R.id.livro3)
        val filtrosAlugados: TextView = findViewById(R.id.filtroAlugados)
        val filtrosLidos: TextView = findViewById(R.id.filtroLidos)
        val filtrosAZ: TextView = findViewById(R.id.filtroAZ)

        btnBack.setOnClickListener {
            var intencao = Intent(this, TelaMenuEmprestUsu::class.java)
            startActivity(intencao)
        }
        val abrirDetalhes = { nomeLivro: String ->
            val intent = Intent(this, TelaInfoLivroUsu::class.java)
            intent.putExtra("livro_nome", nomeLivro)
            startActivity(intent)
        }

        livro1.setOnClickListener { abrirDetalhes("Java Como Programar") }
        livro2.setOnClickListener { abrirDetalhes("Java Avançado") }
        livro3.setOnClickListener { abrirDetalhes("Ciência da Computação") }

        btnAvaliarLivros.setOnClickListener {
            var intencao = Intent(this, TelaReviewUsu::class.java)
            startActivity(intencao)
        }
    }
}



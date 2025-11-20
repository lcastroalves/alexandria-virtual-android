package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroAdapterMeus
import com.example.alexandriavirtual20.model.Livro

class TelaMeusLivrosUsu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_meus_livros_usu)

        val btnBack: ImageButton = findViewById(R.id.btnback)
        val btnAvaliar: Button = findViewById(R.id.btnAvaliarLivros)
        val txtProgresso: TextView = findViewById(R.id.txtProgresso)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerLivros)
        val filtrosAlugados: TextView = findViewById(R.id.filtroAlugados)
        val filtrosLidos: TextView = findViewById(R.id.filtroLidos)
        val filtrosAZ: TextView = findViewById(R.id.filtroAZ)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val listaLivros = listOf(
            Livro("111111111", "Ciências da Computação", "Ernanne Rosa Martins", "Ernanne Rosa Martins", "", "130")
        )

        val adapter = LivroAdapterMeus(listaLivros) { livro ->
            val intent = Intent(this, TelaInfoLivroUsu::class.java)
            intent.putExtra("livroId", livro.id)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter


        txtProgresso.text = "${listaLivros.size}/12"

        btnAvaliar.setOnClickListener {
            startActivity(Intent(this, TelaReviewUsu::class.java))
        }
    }
}

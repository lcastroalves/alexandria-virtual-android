package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.alexandriavirtual20.model.Livro
import kotlin.collections.joinToString
import kotlin.jvm.java

class TelaRetirarLivroUsu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_retirar_livro_usu)
        val btnBack: ImageButton = findViewById(R.id.btnback)
        val btnConfirmar: Button = findViewById(R.id.btnConfirmar)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Livro solicitado com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }
        val livrosSelecionados = intent.getParcelableArrayListExtra<Livro>("livrosSelecionados")

        if (livrosSelecionados != null) {
            val nomes = livrosSelecionados.joinToString("\n") { it.titulo }
            Toast.makeText(this, "Livros recebidos:\n$nomes", Toast.LENGTH_LONG).show()
        }


    }
}
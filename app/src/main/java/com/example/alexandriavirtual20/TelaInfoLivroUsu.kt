package com.example.alexandriavirtual20

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.content.Intent
class TelaInfoLivroUsu : AppCompatActivity  () {

    var anterior: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_info_livro_usu)

        val botaoVoltar = findViewById<ImageButton>(R.id.botaoVoltar)

        anterior = intent.getStringExtra("anterior")
        botaoVoltar.setOnClickListener {

            // BOTAO VOLTAR
            onBackPressed()
        }
    }
}
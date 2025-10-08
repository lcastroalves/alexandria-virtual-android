package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaAvaliacoesUsu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_avaliacoes_usu)
        val btnvoltar : ImageButton = findViewById(R.id.btnvoltar)

        btnvoltar.setOnClickListener {
            var intencao = Intent(this, TelaEmprestLivrosUsu::class.java)
            startActivity(intencao)
        }




    }
}
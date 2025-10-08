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

class TelaRetirarLivroUsu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_retirar_livro_usu)
        val btnBack: ImageButton = findViewById(R.id.btnvoltar)
        val btnConfirmar: Button = findViewById(R.id.btnConfirmar)

        btnBack.setOnClickListener {
            var intencao = Intent(this, TelaEmprestLivrosUsu::class.java)
            startActivity(intencao)
        }
        btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Livro Solicitado com Sucesso", Toast.LENGTH_SHORT).show()
        }

    }
}
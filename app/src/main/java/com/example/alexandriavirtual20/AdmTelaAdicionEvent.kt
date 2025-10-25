package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AdmTelaAdicionEvent : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnAdicionar: Button
    private lateinit var btnADicionarImagem: ImageButton
    private lateinit var titulo: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_add_evento)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnAdicionar = findViewById(R.id.salvarEvento)
        btnADicionarImagem = findViewById(R.id.addImagemEvento) // Não há nada que possamos fazer por enquanto
        titulo = findViewById(R.id.addTituloEvento)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnAdicionar.setOnClickListener {
            if (titulo.text.toString() == "Meu vizinho totoro") {
                Toast.makeText(
                    this,
                    "Evento já cadastrado! Confira os dados ou insira um novo evento",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Evento salvo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
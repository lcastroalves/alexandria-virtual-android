package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class AdmTelaEditEvent : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnSalvar: Button
    private lateinit var titulo: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_edit_event)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnSalvar = findViewById(R.id.botaoSalvarEditarEvento)
        titulo = findViewById(R.id.tituloEditarEvento)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSalvar.setOnClickListener {
            if (titulo.text.toString() == "Meu vizinho totoro") { //Investigar como fazer para ele cair no if
                Toast.makeText(this, "Evento já cadastrado! Confira os dados ou insira um novo evento", Toast.LENGTH_SHORT).show() //Alterar pop-up para toast nos requisitos
        } else {
                Toast.makeText(this, "Evento salvo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
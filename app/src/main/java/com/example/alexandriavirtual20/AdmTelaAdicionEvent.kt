package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AdmTelaAdicionEvent : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnAdicionar: Button
    private lateinit var btnADicionarImagem: ImageButton
    private lateinit var titulo: TextInputEditText
    private lateinit var abrirGaleria: ActivityResultLauncher<String>
    private lateinit var imagemEvento: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_add_evento)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnAdicionar = findViewById(R.id.salvarEvento)
        btnADicionarImagem = findViewById(R.id.editImagemEvento)
        titulo = findViewById(R.id.addTituloEvento)
        imagemEvento = findViewById(R.id.imagemEventoAdd)


        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        abrirGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){uri ->
            if(uri != null){
                // imagem capturada
                imagemEvento.setImageURI(uri)
            }
        }

        btnADicionarImagem.setOnClickListener {
            abrirGaleria.launch("image/*")
        }

        btnAdicionar.setOnClickListener {
            if (titulo.text.toString() == "Meu vizinho totoro") {
                Toast.makeText(
                    this,
                    "Evento já cadastrado! Confira os dados ou insira um novo evento",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Evento adicionado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
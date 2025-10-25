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

class AdmTelaEditEvent : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnSalvar: Button
    private lateinit var btnEditarImagem: ImageButton
    private lateinit var titulo: TextInputEditText
    private lateinit var abrirGaleria: ActivityResultLauncher<String>
    private lateinit var imagemEvento: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_edit_event)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnSalvar = findViewById(R.id.botaoSalvarEditarEvento)
        titulo = findViewById(R.id.tituloEditarEvento)
        imagemEvento = findViewById(R.id.imagemEventoEdit)
        btnEditarImagem = findViewById(R.id.editImagemEvento)


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

        btnEditarImagem.setOnClickListener {
            abrirGaleria.launch("image/*")
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
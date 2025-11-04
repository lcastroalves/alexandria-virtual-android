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
import com.google.firebase.firestore.FirebaseFirestore

class AdmTelaAdicionEvent : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnAdicionar: Button
    private lateinit var btnADicionarImagem: ImageButton
    private lateinit var abrirGaleria: ActivityResultLauncher<String>
    private lateinit var imagemEvento: ImageView
    private lateinit var fb : FirebaseFirestore
    private lateinit var titulo: TextInputEditText
    private lateinit var data: TextInputEditText
    private lateinit var horario: TextInputEditText
    private lateinit var descricao: TextInputEditText
    private lateinit var breveDescricao: TextInputEditText
    private lateinit var local: TextInputEditText
    private lateinit var nomeEvento: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_add_evento)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnAdicionar = findViewById(R.id.salvarEvento)
        btnADicionarImagem = findViewById(R.id.editImagemEvento)
        fb = FirebaseFirestore.getInstance()

        imagemEvento = findViewById(R.id.imagemEventoAdd)
        titulo = findViewById(R.id.addTituloEvento)
        horario = findViewById(R.id.addHorarioEvento)
        data = findViewById(R.id.addDataEvento)
        descricao = findViewById(R.id.addDescricaoEvento)
        breveDescricao = findViewById(R.id.addDescricaoBreveEvento)
        local = findViewById(R.id.addLocalEvento)
        //Informações que serão passadas ao banco de dados

        nomeEvento = titulo.text.toString().trim().uppercase()

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
            verificarCamposEAdd()
        }
    }
    private fun campoVazio(): Boolean {
        val campos: List<TextInputEditText> = listOf(titulo, data, horario, descricao, breveDescricao, local)

        return campos.any { it.text.isNullOrEmpty() }
    }
    // Função para conferir se todos os campos estão preenchidos

    private fun verificarCamposEAdd() {
        if (campoVazio()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        nomeEvento = titulo.text.toString().trim()

        fb.collection("evento")
            .whereEqualTo("nome", nomeEvento)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    enviarDados()
                }
                else {
                    Toast.makeText(this, "Evento já cadastrado! Confira os dados ou insira um novo evento", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun enviarDados() {
        val dadosEvento = hashMapOf(
            "nome" to nomeEvento,
            "data" to data.text.toString().trim(),
            "horario" to horario.text.toString().trim(),
            "descricao" to descricao.text.toString().trim(),
            "breveDescricao" to breveDescricao.text.toString().trim(),
            "local" to local.text.toString().trim(),
            "imagem" to imagemEvento
        )
        fb.collection("evento").add(dadosEvento)
            .addOnSuccessListener {
                Toast.makeText(this, "Evento adicionado", Toast.LENGTH_SHORT).show()
            }
    }
}
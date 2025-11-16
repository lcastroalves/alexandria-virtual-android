package com.example.alexandriavirtual20

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class AdmTelaAdicionEvent : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnAdicionar: Button
    private lateinit var btnADicionarImagem: ImageButton
    private lateinit var titulo: TextInputEditText
    private lateinit var abrirGaleria: ActivityResultLauncher<String>
    private lateinit var imagemEvento: ImageView
    private lateinit var fb : FirebaseFirestore
    private lateinit var data: TextInputEditText
    private lateinit var horario: TextInputEditText
    private lateinit var descricao: TextInputEditText
    private lateinit var breveDescricao: TextInputEditText
    private lateinit var local: TextInputEditText
    private lateinit var nomeEvento: String
    private var colocouImagem: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_add_evento)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnAdicionar = findViewById(R.id.salvarEvento)
        btnADicionarImagem = findViewById(R.id.editImagemEvento)
        fb = FirebaseFirestore.getInstance()
        fb.clearPersistence()

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
                colocouImagem = true
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

        return campos.any { it.text.isNullOrEmpty() } || !colocouImagem
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
            .addOnFailureListener { querySnapshot ->
                Toast.makeText(this, "Erro ao verificar evento", Toast.LENGTH_SHORT).show()
            }
    }

    private fun enviarDados() {
        val campos: List<TextInputEditText> = listOf(titulo, data, horario, descricao, breveDescricao, local)

        val imagemEnviada = (imagemEvento.drawable as? BitmapDrawable)?.bitmap
        val imagemBase64 = if (imagemEnviada != null) {
            val outputStream = ByteArrayOutputStream()
            imagemEnviada.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } else {
            ""
        }

        val formatoData = java.text.SimpleDateFormat("dd/MM/yy")
        val date = formatoData.parse(data.text.toString())
        val timestampData = com.google.firebase.Timestamp(date)

        val dadosEvento = hashMapOf(
            "nome" to nomeEvento,
            "data" to data.text.toString(),
            "timestampData" to timestampData,
            "horario" to horario.text.toString(),
            "descricao" to descricao.text.toString(),
            "breveDescricao" to breveDescricao.text.toString(),
            "local" to local.text.toString(),
            "imagem" to imagemBase64
        )
        fb.collection("evento").add(dadosEvento)
            .addOnSuccessListener {
                Toast.makeText(this, "Evento adicionado", Toast.LENGTH_SHORT).show()
                campos.forEach {
                    it.text?.clear()
                }

                imagemEvento.setImageResource(R.drawable.padraopng)
                colocouImagem = false
            }
    }
}
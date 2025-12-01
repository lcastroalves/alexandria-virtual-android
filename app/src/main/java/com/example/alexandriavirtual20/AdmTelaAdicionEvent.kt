package com.example.alexandriavirtual20

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.util.Locale

class AdmTelaAdicionEvent : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnAdicionar: Button
    private lateinit var btnADicionarImagem: ImageButton
    private lateinit var titulo: TextInputEditText
    private lateinit var abrirGaleria: ActivityResultLauncher<String>
    private lateinit var imagemEvento: ImageView
    private lateinit var fb: FirebaseFirestore
    private lateinit var data: TextInputEditText
    private lateinit var horario: TextInputEditText
    private lateinit var descricao: TextInputEditText
    private lateinit var breveDescricao: TextInputEditText
    private lateinit var local: TextInputEditText
    private lateinit var nomeEvento: String
    private var colocouImagem = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_add_evento)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnAdicionar = findViewById(R.id.btnSalvar)
        btnADicionarImagem = findViewById(R.id.btnMudarImgEvento)
        fb = FirebaseFirestore.getInstance()

        imagemEvento = findViewById(R.id.imagemEventoAdd)
        titulo = findViewById(R.id.addTituloEvento)
        horario = findViewById(R.id.addHorarioEvento)
        data = findViewById(R.id.addDataEvento)
        descricao = findViewById(R.id.addDescricaoEvento)
        breveDescricao = findViewById(R.id.addDescricaoBreveEvento)
        local = findViewById(R.id.addLocalEvento)

        btnVoltar.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        abrirGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
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
        val campos = listOf(titulo, data, horario, descricao, breveDescricao, local)
        return campos.any { it.text.isNullOrEmpty() } || !colocouImagem
    }

    private fun verificarCamposEAdd() {
        nomeEvento = titulo.text.toString()

        if (campoVazio()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        fb.collection("evento")
            .whereEqualTo("nome", nomeEvento)
            .get()
            .addOnSuccessListener { query ->
                if (query.isEmpty) enviarDados()
                else Toast.makeText(this, "Evento já cadastrado!", Toast.LENGTH_SHORT).show()
            }
    }

    // 🔥 Função para COMPRIMIR imagem antes de enviar ao banco
    private fun comprimirImagem(bitmap: Bitmap): String {
        // Reduz resolução para no máximo 800px
        val larguraMax = 800
        val ratio = larguraMax.toFloat() / bitmap.width
        val alturaReduzida = (bitmap.height * ratio).toInt()

        val bitmapReduzido = Bitmap.createScaledBitmap(bitmap, larguraMax, alturaReduzida, true)

        val output = ByteArrayOutputStream()

        // JPEG com compressão REAL (70%)
        bitmapReduzido.compress(Bitmap.CompressFormat.JPEG, 70, output)

        return Base64.encodeToString(output.toByteArray(), Base64.DEFAULT)
    }

    private fun enviarDados() {
        val campos = listOf(titulo, data, horario, descricao, breveDescricao, local)

        val bitmapOriginal = (imagemEvento.drawable as? BitmapDrawable)?.bitmap
        val imagemBase64 = if (bitmapOriginal != null) {
            comprimirImagem(bitmapOriginal)   // ✔ agora envia comprimido
        } else {
            ""
        }

        val formatoData = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

        val timestampData = try {
            val date = formatoData.parse(data.text.toString())
            Timestamp(date!!)
        } catch (e: Exception) {
            Toast.makeText(this, "Data inválida. Use dd/mm/aa.", Toast.LENGTH_SHORT).show()
            return
        }

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
                Toast.makeText(this, "Evento adicionado!", Toast.LENGTH_SHORT).show()
                campos.forEach { it.text?.clear() }
                imagemEvento.setImageResource(R.drawable.padraopng)
                colocouImagem = false
            }
    }
}

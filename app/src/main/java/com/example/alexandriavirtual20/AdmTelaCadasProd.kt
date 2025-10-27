package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AdmTelaCadasProd : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnMudarImg: ImageButton
    private lateinit var capaLivro: ImageView
    private lateinit var abrirGaleria: ActivityResultLauncher<String>
    private lateinit var btnCadastrar: Button

    private lateinit var titulo: TextInputEditText
    private lateinit var subtitulo: TextInputEditText
    private lateinit var autor: TextInputEditText
    private lateinit var categoria: TextInputEditText
    private lateinit var anoPub: TextInputEditText
    private lateinit var edicao: TextInputEditText
    private lateinit var unidade: TextInputEditText
    private lateinit var idioma: TextInputEditText
    private lateinit var sinopse: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adm_tela_cadas_prod)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnMudarImg = findViewById(R.id.btnMudarImg)
        capaLivro = findViewById(R.id.capaEvento)
        btnCadastrar = findViewById(R.id.btnCadastrar)

        titulo = findViewById(R.id.titulo)
        subtitulo = findViewById(R.id.subtitulo)
        autor = findViewById(R.id.autor)
        categoria = findViewById(R.id.categoria)
        anoPub = findViewById(R.id.anoPub)
        edicao = findViewById(R.id.edicao)
        unidade = findViewById(R.id.unidade)
        idioma = findViewById(R.id.idioma)
        sinopse = findViewById(R.id.sinopse)

        abrirGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                capaLivro.setImageURI(uri)
            }
        }

        btnMudarImg.setOnClickListener {
            abrirGaleria.launch("image/*")
        }

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnCadastrar.setOnClickListener {
            val campos = listOf(
                titulo.text.toString(),
                subtitulo.text.toString(),
                autor.text.toString(),
                categoria.text.toString(),
                anoPub.text.toString(),
                edicao.text.toString(),
                unidade.text.toString(),
                idioma.text.toString(),
                sinopse.text.toString()
            )

            if (campos.any { it.isBlank() }) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Produto Cadastrado!", Toast.LENGTH_SHORT).show()
        }
    }
}

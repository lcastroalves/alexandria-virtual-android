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

class AdmTelaCadasProd : AppCompatActivity() {

    private lateinit var btnVoltar : ImageButton
    private lateinit var btnMudarImg : ImageButton
    private lateinit var capaLivro : ImageView
    private lateinit var abrirGaleria : ActivityResultLauncher<String>
    private lateinit var btnCadastrar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adm_tela_cadas_prod)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnMudarImg = findViewById(R.id.btnMudarImg)
        capaLivro = findViewById(R.id.capaEvento)
        btnCadastrar = findViewById(R.id.btnCadastrar)

        abrirGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){uri ->
            if (uri != null){
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
            Toast.makeText(this, "Produto Cadastrado!", Toast.LENGTH_SHORT).show()
        }
    }
}
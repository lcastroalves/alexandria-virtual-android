package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdmTelaEditarProduto : AppCompatActivity() {

    private lateinit var btnVoltar : ImageButton
    private lateinit var btnMudarImg : ImageButton
    private lateinit var capaLivro : ImageView
    private lateinit var abrirGaleria : ActivityResultLauncher<String>
    private lateinit var btnSalvar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adm_tela_editar_produto)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnMudarImg = findViewById(R.id.btnMudarImg)
        capaLivro = findViewById(R.id.capaLivro)
        btnSalvar = findViewById(R.id.btnSalvar)

        abrirGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){uri ->
            if (uri != null){
                capaLivro.setImageURI(uri)
            }
        }

        btnMudarImg.setOnClickListener {
            abrirGaleria.launch("capaLivro/*")
        }

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSalvar.setOnClickListener {
            Toast.makeText(this, "Alterações salvas", Toast.LENGTH_SHORT).show()
        }

    }
}
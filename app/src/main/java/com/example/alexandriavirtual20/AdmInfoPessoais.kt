package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdmInfoPessoais : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnRedefinirSenha: Button
    private lateinit var btnSalvarAlteracoes: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_info_pessoais)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnRedefinirSenha = findViewById(R.id.botaoRedefinirSenha)
        btnSalvarAlteracoes = findViewById(R.id.botaoSalvarAlteracoes)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnRedefinirSenha.setOnClickListener {
            val intent = Intent(this, TelaRedefinirSenha::class.java)
            startActivity(intent)
        }

        btnSalvarAlteracoes.setOnClickListener {
            Toast.makeText(this, "Alterações salvas", Toast.LENGTH_SHORT).show()
        }
    }
}
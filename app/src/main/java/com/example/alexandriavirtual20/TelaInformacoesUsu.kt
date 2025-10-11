package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaInformacoesUsu : AppCompatActivity() {

    lateinit var btnVoltar : ImageButton
    lateinit var btnRedefinirSenha: Button
    lateinit var btnSalvar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_informacoes_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnRedefinirSenha = findViewById(R.id.botaoRedefinirSenha)
        btnSalvar = findViewById(R.id.botaoSalvar)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnRedefinirSenha.setOnClickListener{
            val intent = Intent(this, TelaRedefinirSenha::class.java);
            startActivity(intent)
        }

        btnSalvar.setOnClickListener {
            // Lembrar de fazer lógica de checagem dos campos
            Toast.makeText(this, "Alterações salvas", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        // logica para salvar as novas alterações
    }
}
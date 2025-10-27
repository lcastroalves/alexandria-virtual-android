package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRedefinirSenha : AppCompatActivity() {

    private lateinit var btnVoltar : ImageButton
    private lateinit var btnRedefinirSenha: Button
    private lateinit var senhaAtual : EditText
    private lateinit var senha1 : EditText
    private lateinit var senha2 : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_redefinir_senha)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnRedefinirSenha = findViewById(R.id.botaoRedefinir)
        senhaAtual = findViewById(R.id.senhaAtual)
        senha1 = findViewById(R.id.novaSenha)
        senha2 = findViewById(R.id.segundaSenha)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnRedefinirSenha.setOnClickListener {
            val senhaAtual = senhaAtual.text.toString()
            val novaSenha = senha1.text.toString()
            val confirmarSenha = senha2.toString()

            if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (novaSenha != confirmarSenha) {
                Toast.makeText(this, "Senhas não correspondem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }
}
package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
class TelaCadastro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_cadastro)

        val botaoCadastrar = findViewById<Button>(R.id.botaoCadastrar)
        val botaoTenhoCadastro = findViewById<Button>(R.id.botaoTenhoCadastro)

        botaoCadastrar.setOnClickListener {
            val intent = Intent(this, AMain::class.java)
            startActivity(intent)
            finish()
        }

        botaoTenhoCadastro.setOnClickListener {
            val intent = Intent (this, TelaLogin::class.java)
            startActivity(intent)
            finish()
        }
    }
}
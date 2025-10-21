package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.util.Log.e
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java

class TelaLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_login)

        val edtEmail: TextView = findViewById(R.id.emailLogin)
        val edtSenha: TextView = findViewById(R.id.senhaLogin)
        val txtEsqueceuSenha: TextView = findViewById(R.id.esqueciSenha)
        val btnEntrar: Button = findViewById(R.id.botaoEntrar)
        val txtCadastrar: TextView = findViewById(R.id.txtCadastrar)

        btnEntrar.setOnClickListener {
            val email = edtEmail.text.toString()
            val senha = edtSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else if (email == "alexandria@gmail.com" && senha == "1234") {
                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()


                val intent = Intent(this, AMain::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Credenciais incorretas, tente novamente!", Toast.LENGTH_SHORT).show()
            }
        }

        txtEsqueceuSenha.setOnClickListener {
            var intent = Intent(this, TelaEmail::class.java )
            startActivity(intent)
        }

        txtCadastrar.setOnClickListener {
            val intent = Intent(this, TelaCadastro::class.java)
            startActivity(intent)
        }
    }
}




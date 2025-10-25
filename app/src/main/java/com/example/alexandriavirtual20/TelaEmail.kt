package com.example.alexandriavirtual20

import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaEmail : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var edtEmail: EditText
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_email)

        btnVoltar = findViewById(R.id.botaoVoltar)
        edtEmail = findViewById(R.id.editarEmail)
        btnEnviar = findViewById(R.id.botaoEnviar)


        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnEnviar.setOnClickListener {
            val email = edtEmail.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else if (email == "alexandria@gmail.com") {
                Toast.makeText(this, "E-mail enviado!", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
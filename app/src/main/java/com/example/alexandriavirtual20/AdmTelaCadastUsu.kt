package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AdmTelaCadastUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var editNome: TextInputEditText
    private lateinit var editUsuario: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editSenha: TextInputEditText
    private lateinit var editConfSenha: TextInputEditText
    private lateinit var btnSalvar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_usu_cadast)

        btnVoltar = findViewById(R.id.botaoVoltar6)
        editNome = findViewById(R.id.edNome)
        editUsuario = findViewById(R.id.edUsuario)
        editEmail = findViewById(R.id.edEmail)
        editSenha = findViewById(R.id.edSenha)
        editConfSenha = findViewById(R.id.edConfSenha)
        btnSalvar = findViewById(R.id.btnSalvar)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSalvar.setOnClickListener {
            val nome    = editNome.text .toString()
            val usuario = editUsuario.text.toString()
            val email   = editEmail.text.toString()
            val senha   = editSenha.text.toString()
            val confSenha = editConfSenha.text.toString()

            if (nome.isEmpty() || usuario.isEmpty() || email.isEmpty() || senha.isEmpty() || confSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
            else if (senha != confSenha) {
                Toast.makeText(this, "As senhas não correspondem", Toast.LENGTH_SHORT).show()
            }
            else if (nome == "Narak Oliveira") {
                Toast.makeText(this, "Usuário já cadastrado! Confira os dados ou adicione um novo usuário", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Usuário salvo com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
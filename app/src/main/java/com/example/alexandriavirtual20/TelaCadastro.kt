package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.EditText
import android.widget.Toast

class TelaCadastro : AppCompatActivity() {

    private lateinit var botaoCadastrar : Button
    private lateinit var botaoTenhoCadastro : Button
    private val emailsCadastrados = listOf("alexandria@gmail.com",
                                            "adm@gmail.br"
                                        )

    private lateinit var inputNomeComp : EditText
    private lateinit var inputNomeUsu: EditText
    private lateinit var inputEmail : EditText
    private lateinit var inputSenha1 : EditText
    private lateinit var inputSenha2 : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_cadastro)

        botaoCadastrar = findViewById(R.id.botaoCadastrar)
        botaoTenhoCadastro = findViewById(R.id.botaoTenhoCadastro)
        inputNomeComp = findViewById(R.id.nomeCompleto)
        inputNomeUsu = findViewById(R.id.nomeUsuario)
        inputEmail = findViewById(R.id.email)
        inputSenha1 = findViewById(R.id.senha)
        inputSenha2 = findViewById(R.id.segundaSenha)

        botaoCadastrar.setOnClickListener {
            val nomeCompleto = inputNomeComp.text.toString()
            val nomeUsuario = inputNomeUsu.text.toString()
            val email = inputEmail.text.toString()
            val senha = inputSenha1.text.toString()
            val confirmarSenha = inputSenha2.text.toString()

            if (nomeCompleto.isEmpty() || nomeUsuario.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Valida email já cadastrado
            if (emailsCadastrados.contains(email)) {
                Toast.makeText(this, "O e-mail informado já está em uso", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Valida a senha
            val senhaValida = senha.length >= 8 &&
                    senha.any { it.isDigit() }

            if (!senhaValida) {
                Toast.makeText(
                    this,
                    "A senha deve ter pelo menos 8 caracteres, incluindo um número.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // As duas senhas iguais
            if (senha != confirmarSenha) {
                Toast.makeText(this, "Senhas não correspondem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Se tudo certo, ta liberado
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
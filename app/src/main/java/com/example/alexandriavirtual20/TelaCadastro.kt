package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import com.example.alexandriavirtual20.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class TelaCadastro : AppCompatActivity() {

    private lateinit var botaoCadastrar : Button
    private lateinit var botaoTenhoCadastro : Button

    private lateinit var inputNomeComp : EditText
    private lateinit var inputNomeUsu: EditText
    private lateinit var inputEmail : EditText
    private lateinit var inputSenha1 : EditText
    private lateinit var inputSenha2 : EditText

    private lateinit var fireBase : FirebaseFirestore
    private lateinit var fbAuth: FirebaseAuth

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

        fireBase = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

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




            // Valida a senha
            val senhaValida = senha.length >= 8 && senha.any { it.isDigit() }
            if (!senhaValida) {
                Toast.makeText(this, "A senha deve ter pelo menos 8 caracteres, incluindo um número.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // As duas senhas iguais
            if (senha != confirmarSenha) {
                Toast.makeText(this, "Senhas não correspondem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Formato de E-mail inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Se tudo certo, confere o email e cria o usuario
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val userID = task.result?.user?.uid ?: return@addOnCompleteListener

                    val usuario = Usuario(
                        id = userID,
                        nome = nomeCompleto,
                        usuario = nomeUsuario,
                        email = email,
                        senha = senha,
                        fotoPerfil = null,
                        isAdmin = false
                    )

                    fireBase.collection("usuario").document(userID).set(usuario).addOnSuccessListener {
                        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, TelaLogin::class.java))
                        finish()
                    }

                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        // Email já está em uso
                        Toast.makeText(this, "E-mail já está cadastrado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        botaoTenhoCadastro.setOnClickListener {
            val intent = Intent (this, TelaLogin::class.java)
            startActivity(intent)
            finish()
        }
    }
}
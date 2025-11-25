package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaRedefinirSenha : AppCompatActivity() {

    private lateinit var fbAuth : FirebaseAuth
    private lateinit var fb: FirebaseFirestore
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

        fb = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnRedefinirSenha.setOnClickListener {
            checagemSenhas()
        }
    }

    private fun checagemSenhas() {
        val senhaAtual = senhaAtual.text.toString()
        val novaSenha = senha1.text.toString()
        val confirmarSenha = senha2.text.toString()

        val usuarioAtual = fbAuth.currentUser
        val uid = usuarioAtual?.uid ?: run {
            Toast.makeText(this, "Usuário não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show()
            return
        }

        if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        fb.collection("usuario")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                if (!doc.exists()) {
                    Toast.makeText(this, "Dados do usuário não encontrados.", Toast.LENGTH_SHORT)
                        .show()
                    return@addOnSuccessListener
                }

                val senhaAtualCorreta = doc.getString("senha")

                if (senhaAtualCorreta.isNullOrEmpty()) {
                    Toast.makeText(
                        this,
                        "Senha atual não encontrada no cadastro.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnSuccessListener
                }

                val senhaValida = novaSenha.length >= 8 && novaSenha.any { it.isDigit() }
                if (!senhaValida) {
                    Toast.makeText(this, "A senha deve ter pelo menos 8 caracteres, incluindo um número.", Toast.LENGTH_LONG).show()
                    return@addOnSuccessListener
                }

                if (senhaAtual != senhaAtualCorreta) {
                    Toast.makeText(this, "Senha atual incorreta.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                if (novaSenha != confirmarSenha) {
                    Toast.makeText(this, "Senhas não correspondem", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                updateNovaSenha(novaSenha, uid)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao buscar usuário.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateNovaSenha(novaSenha: String, uid: String){
        fb.collection("usuario")
            .document(uid)
            .update("senha", novaSenha)
            .addOnSuccessListener {
                Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar nova senha.", Toast.LENGTH_SHORT).show()
            }
    }
}



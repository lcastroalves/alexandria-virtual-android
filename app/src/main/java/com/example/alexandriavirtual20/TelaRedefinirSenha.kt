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

        val senhaAtualTxt = senhaAtual.text.toString()
        val novaSenha = senha1.text.toString()
        val confirmarSenha = senha2.text.toString()

        val usuarioAtual = fbAuth.currentUser
        val email = usuarioAtual?.email ?: run {
            return
        }

        if (senhaAtualTxt.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (novaSenha != confirmarSenha) {
            Toast.makeText(this, "Senhas não correspondem", Toast.LENGTH_SHORT).show()
            return
        }

        // Regra de senha
        val senhaValida = novaSenha.length >= 8 && novaSenha.any { it.isDigit() }
        if (!senhaValida) {
            Toast.makeText(this, "A senha deve ter pelo menos 8 caracteres e um número.", Toast.LENGTH_LONG).show()
            return
        }

        // 🔥 REAUTENTICAR usuário com email + senha ATUAL
        val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, senhaAtualTxt)

        usuarioAtual.reauthenticate(credential)
            .addOnSuccessListener {
                // Agora pode atualizar a senha no FirebaseAuth
                usuarioAtual.updatePassword(novaSenha)
                    .addOnSuccessListener {

                        // Atualiza no Firestore também (opcional)
                        fb.collection("usuario")
                            .document(usuarioAtual.uid)
                            .update("senha", novaSenha)

                        Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao alterar senha: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Senha atual incorreta!", Toast.LENGTH_SHORT).show()
            }
    }
}



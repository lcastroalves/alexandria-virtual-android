package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class AdmTelaCadastUsu : AppCompatActivity() {
    private lateinit var fb : FirebaseFirestore
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

        fb = FirebaseFirestore.getInstance()
        btnVoltar = findViewById(R.id.botaoVoltar6)
        btnSalvar = findViewById(R.id.btnSalvar)

        editNome = findViewById(R.id.edNome)
        editUsuario = findViewById(R.id.edUsuario)
        editEmail = findViewById(R.id.edEmail)
        editSenha = findViewById(R.id.edSenha)
        editConfSenha = findViewById(R.id.edConfSenha)



        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSalvar.setOnClickListener {
            tentarCadastrar()
        }
    }

    private fun tentarCadastrar() {
        val nome = editNome.text?.toString()?.trim().orEmpty()
        val usuario = editUsuario
            .text?.toString()?.trim()?.lowercase()?.replace("[^a-z0-9._-]".toRegex(), "").orEmpty()
        //Lara castro = laracastro e etc
        val email = editEmail.text?.toString()?.trim().orEmpty()
        val senha = editSenha.text?.toString()?.trim().orEmpty()
        val confSenha = editConfSenha.text?.toString()?.trim().orEmpty()

        //algum campo vazio?
        if (nome.isEmpty() || usuario.isEmpty() || email.isEmpty() || senha.isEmpty() || confSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        //senha válida?
        if (senha.length < 8 || !senha.any { it.isDigit() }) {
            Toast.makeText(
                this,
                "A senha deve ter pelo menos 8 caracteres, incluindo um número.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        //senha = confSenha?
        if (senha != confSenha) {
            Toast.makeText(this, "As senhas não correspondem", Toast.LENGTH_SHORT).show()
            return
        }

        //usuario existente?
        fb.collection("usuario").whereEqualTo("usuario", usuario).get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {

                    // hashMap
                    val dadosUsuario = hashMapOf(

                        "nome" to nome,
                        "usuario" to usuario,
                        "email" to email,
                        "fotoPerfil" to null,
                        "admin" to false
                    )

                    // envia para o Firebase
                    fb.collection("usuario")
                        .add(dadosUsuario)
                        .addOnSuccessListener { documentReference ->
                            // Pega o ID gerado pelo Firestore
                            val idGerado = documentReference.id

                            // Atualiza o campo "id" do próprio documento
                            documentReference.update("id", idGerado)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                    limparCampos()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Erro ao atualizar ID do usuário.", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao salvar os dados do usuário.", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(this, "E-mail já está cadastrado", Toast.LENGTH_SHORT).show()
                }
        }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao verificar usuário existente.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun limparCampos(){
        editNome.text?.clear()
        editUsuario.text?.clear()
        editEmail.text?.clear()
        editSenha.text?.clear()
        editConfSenha.text?.clear()
    }
}
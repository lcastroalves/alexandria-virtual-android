package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaInformacoesUsu : AppCompatActivity() {

    lateinit var btnVoltar : ImageButton
    lateinit var btnRedefinirSenha: Button
    lateinit var btnSalvar : Button
    lateinit var imagemUsu : ImageView
    lateinit var nomeUsu : TextView
    lateinit var nomeComp : TextInputEditText
    lateinit var inputNomeUsu : TextInputEditText
    lateinit var email : TextInputEditText
    lateinit var senha : TextInputEditText

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var fireBase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_informacoes_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnRedefinirSenha = findViewById(R.id.botaoRedefinirSenha)
        btnSalvar = findViewById(R.id.botaoSalvar)

        imagemUsu = findViewById(R.id.iconUsu)
        nomeUsu = findViewById(R.id.nomeUsu)
        nomeComp = findViewById(R.id.nomeComp)
        inputNomeUsu = findViewById(R.id.inputNomeUsu)
        email = findViewById(R.id.email)
        senha = findViewById(R.id.senha)

        fbAuth = FirebaseAuth.getInstance()
        fireBase = FirebaseFirestore.getInstance()

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnRedefinirSenha.setOnClickListener{
            val intent = Intent(this, TelaRedefinirSenha::class.java);
            startActivity(intent)
        }

        btnSalvar.setOnClickListener {
            salvarAlteracoes()
        }
    }

    override fun onStart() {
        super.onStart()
        carregarDadosUsuario()
    }

    private fun carregarDadosUsuario(){

        val usuario = fbAuth.currentUser

        if (usuario != null){
            fireBase.collection("usuario").document(usuario.uid).get().addOnSuccessListener { doc ->
                if (doc.exists()){

                    val fotoPerfil = doc.getString("fotoPerfil")

                    if (fotoPerfil.isNullOrEmpty()) {
                        imagemUsu.setImageResource(R.drawable.empty_user)
                    } else {
                        try {
                            val decoded = android.util.Base64.decode(fotoPerfil, android.util.Base64.DEFAULT)
                            val bitmap = android.graphics.BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
                            imagemUsu.setImageBitmap(bitmap)
                        } catch (e: Exception) {
                            imagemUsu.setImageResource(R.drawable.empty_user)
                        }
                    }

                    // pega do firestore
                    val nomeCompleto = doc.getString("nome") ?: ""
                    val nomeUsuario = doc.getString("usuario") ?: ""
                    val emailFb = doc.getString("email") ?: ""

                    // atribui aos campos
                    nomeUsu.text = nomeUsuario
                    nomeComp.setText(nomeCompleto)
                    inputNomeUsu.setText(nomeUsuario)
                    email.setText(emailFb)
                }
            }
        }
    }

    private fun salvarAlteracoes(){
        val usuario = fbAuth.currentUser ?: return

        val nomeUsuarioTopoNovo = inputNomeUsu.text.toString()
        val nomeCompletoNovo = nomeComp.text.toString()
        val nomeUsuarioNovo = inputNomeUsu.text.toString()

        val atualiza = mapOf(
            "nome" to nomeCompletoNovo,
            "usuario" to nomeUsuarioNovo
        )

        if (nomeCompletoNovo.isEmpty() || nomeUsuarioNovo.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        fireBase.collection("usuario").document(usuario.uid).update(atualiza).addOnSuccessListener {
            nomeUsu.text = nomeUsuarioTopoNovo
            Toast.makeText(this, "Alterações salvas", Toast.LENGTH_SHORT).show()
        }
    }


}
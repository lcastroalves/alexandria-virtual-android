package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdmInfoPessoais : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnRedefinirSenha: Button
    private lateinit var btnSalvarAlteracoes: Button

    private lateinit var iconAdm : ImageView
    private lateinit var nomeAdmSuperior : TextView
    private lateinit var nomeCompleto: TextInputEditText
    private lateinit var nomeUsuario: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var senha: TextInputEditText
    private lateinit var cargo: TextInputEditText
    private lateinit var contato: TextInputEditText

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var fireBase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_info_pessoais)
        FirebaseApp.initializeApp(this)

        fbAuth = FirebaseAuth.getInstance()
        fireBase = FirebaseFirestore.getInstance()

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnRedefinirSenha = findViewById(R.id.botaoRedefinirSenha)
        btnSalvarAlteracoes = findViewById(R.id.botaoSalvarAlteracoes)

        iconAdm = findViewById(R.id.iconAdm)
        nomeAdmSuperior = findViewById(R.id.nomeAdmSuperior)
        nomeCompleto = findViewById(R.id.nomeComp)       // nome completo
        nomeUsuario = findViewById(R.id.inputNomeUsu)    // nome do usuário
        email = findViewById(R.id.email)
        senha = findViewById(R.id.senha)
        cargo = findViewById(R.id.cargo)
        contato = findViewById(R.id.contato)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnRedefinirSenha.setOnClickListener {
            val intent = Intent(this, TelaRedefinirSenha::class.java)
            startActivity(intent)
        }

        btnSalvarAlteracoes.setOnClickListener {
            salvarAlteracoes()
        }
    }

    override fun onStart() {
        super.onStart()
        carregarDadosAdm()
    }

    private fun carregarDadosAdm() {

        val admin = fbAuth.currentUser

        if (admin != null){
            fireBase.collection("usuario").document(admin.uid).get().addOnSuccessListener { doc ->
                if (doc.exists()){

                    val fotoPerfil = doc.getString("fotoPerfil")

                    if (fotoPerfil.isNullOrEmpty()) {
                        iconAdm.setImageResource(R.drawable.empty_user)
                    } else {
                        try {
                            val decoded = android.util.Base64.decode(fotoPerfil, android.util.Base64.DEFAULT)
                            val bitmap = android.graphics.BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
                            iconAdm.setImageBitmap(bitmap)
                        } catch (e: Exception) {
                            iconAdm.setImageResource(R.drawable.empty_user)
                        }
                    }

                    // pega do firestore
                    val nomeCompletoA = doc.getString("nome") ?: ""
                    val nomeAdm = doc.getString("usuario") ?: ""
                    val emailFb = doc.getString("email") ?: ""
                    val cargoAdm = doc.getString("cargo") ?: ""
                    val contatoAdm = doc.getString("contato") ?: ""

                    // atribui aos campos
                    nomeAdmSuperior.text = nomeAdm
                    nomeCompleto.setText(nomeCompletoA)
                    nomeUsuario.setText(nomeAdm)
                    email.setText(emailFb)
                    cargo.setText(cargoAdm)
                    contato.setText(contatoAdm)
                }
            }  .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun salvarAlteracoes(){
        val usuario = fbAuth.currentUser ?: return

        val nomeUsuarioTopoNovo = nomeUsuario.text.toString()
        val nomeCompletoNovo = nomeCompleto.text.toString()
        val nomeUsuarioNovo = nomeUsuario.text.toString()
        val cargoNovo = cargo.text.toString()
        val contatoNovo = contato.text.toString()

        val atualiza = mapOf(
            "nome" to nomeCompletoNovo,
            "usuario" to nomeUsuarioNovo,
            "cargo" to cargoNovo,
            "contato" to contatoNovo
        )

        if (nomeCompletoNovo.isEmpty() || nomeUsuarioNovo.isEmpty() || cargoNovo.isEmpty() || contatoNovo.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        fireBase.collection("usuario").document(usuario.uid).update(atualiza).addOnSuccessListener {
            nomeAdmSuperior.text = nomeUsuarioTopoNovo
            Toast.makeText(this, "Alterações salvas", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun preencherCamposComDoc(id: String, data: MutableMap<String, Any>?) {
//        if (data == null) return
//
//        nomeCompleto.setText(data["nomeCompleto"] as? String ?: data["nome"] as? String ?: "")
//        nomeUsuario.setText(data["nomeUsuario"] as? String ?: data["usuario"] as? String ?: "")
//        email.setText(data["email"] as? String ?: "")
//        senha.setText(data["senha"] as? String ?: "")
//
//        cargo.setText("")
//        contato.setText("")
//    }
//
//    private fun camposVazios(): Boolean {
//        val campos = listOf(nomeCompleto, nomeUsuario, email, senha, cargo, contato)
//        return campos.any { it.text.isNullOrEmpty() }
//    }
//
//    private fun verificarCamposEAtualizar() {
//        if (camposVazios()) {
//            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val emailAdm = email.text.toString().trim()
//
//        fireBase.collection("administradores")
//            .whereEqualTo("email", emailAdm)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                if (querySnapshot.isEmpty) {
//                    salvarNovoAdm()
//                } else {
//                    val docId = querySnapshot.documents[0].id
//                    atualizarAdmExistente(docId)
//                }
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
//            }
//    }
//
//    private fun salvarNovoAdm() {
//        val dadosAdm = hashMapOf(
//            "nomeCompleto" to nomeCompleto.text.toString().trim(),
//            "nomeUsuario" to nomeUsuario.text.toString().trim(),
//            "email" to email.text.toString().trim(),
//            "senha" to senha.text.toString().trim(),
//            "cargo" to cargo.text.toString().trim(),
//            "contato" to contato.text.toString().trim()
//        )
//
//        fb.collection("administradores").add(dadosAdm)
//            .addOnSuccessListener {
//                Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Erro ao salvar os dados.", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun atualizarAdmExistente(docId: String) {
//        val dadosAtualizados = mapOf(
//            "nomeCompleto" to nomeCompleto.text.toString().trim(),
//            "nomeUsuario" to nomeUsuario.text.toString().trim(),
//            "senha" to senha.text.toString().trim(),
//            "cargo" to cargo.text.toString().trim(),
//            "contato" to contato.text.toString().trim()
//        )
//
//        fireBase.collection("administradores").document(docId)
//            .update(dadosAtualizados)
//            .addOnSuccessListener {
//                Toast.makeText(this, "Informações atualizadas com sucesso!", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Erro ao atualizar os dados.", Toast.LENGTH_SHORT).show()
//            }
//    }
}

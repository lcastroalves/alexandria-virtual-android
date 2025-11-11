package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class AdmInfoPessoais : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnRedefinirSenha: Button
    private lateinit var btnSalvarAlteracoes: Button

    private lateinit var nomeCompleto: TextInputEditText
    private lateinit var nomeUsuario: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var senha: TextInputEditText
    private lateinit var cargo: TextInputEditText
    private lateinit var contato: TextInputEditText

    private lateinit var fb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_info_pessoais)
        FirebaseApp.initializeApp(this)

        fb = FirebaseFirestore.getInstance()

        // Referências dos componentes
        btnVoltar = findViewById(R.id.botaoVoltar)
        btnRedefinirSenha = findViewById(R.id.botaoRedefinirSenha)
        btnSalvarAlteracoes = findViewById(R.id.botaoSalvarAlteracoes)

        nomeCompleto = findViewById(R.id.nomeCompleto)
        nomeUsuario = findViewById(R.id.nomeUsuario)
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
            verificarCamposEAtualizar()
        }

        // Carregar os dados do administrador Dillan
        carregarDadosAdm()
    }

    private fun carregarDadosAdm() {
        val emailAdm = "Dillan@gmail.com".trim().lowercase()

        // 🔸 primeiro tenta em "administradores"
        fb.collection("administradores")
            .get()
            .addOnSuccessListener { query ->
                val doc = query.documents.find {
                    it.getString("email")?.trim()?.lowercase() == emailAdm
                }

                if (doc != null) {
                    preencherCamposComDoc(doc.id, doc.data)
                } else {
                    // 🔸 se não achou, tenta na coleção "usuario"
                    fb.collection("usuario")
                        .get()
                        .addOnSuccessListener { queryUser ->
                            val docUser = queryUser.documents.find {
                                it.getString("email")?.trim()?.lowercase() == emailAdm
                            }

                            if (docUser != null) {
                                preencherCamposComDoc(docUser.id, docUser.data)
                            } else {
                                Toast.makeText(this, "Administrador não encontrado.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao buscar em 'usuario'.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao buscar administrador.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun preencherCamposComDoc(id: String, data: MutableMap<String, Any>?) {
        if (data == null) return

        nomeCompleto.setText(data["nomeCompleto"] as? String ?: data["nome"] as? String ?: "")
        nomeUsuario.setText(data["nomeUsuario"] as? String ?: data["usuario"] as? String ?: "")
        email.setText(data["email"] as? String ?: "")
        senha.setText(data["senha"] as? String ?: "")

        cargo.setText("")
        contato.setText("")
    }

    private fun camposVazios(): Boolean {
        val campos = listOf(nomeCompleto, nomeUsuario, email, senha, cargo, contato)
        return campos.any { it.text.isNullOrEmpty() }
    }

    private fun verificarCamposEAtualizar() {
        if (camposVazios()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val emailAdm = email.text.toString().trim()

        fb.collection("administradores")
            .whereEqualTo("email", emailAdm)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    salvarNovoAdm()
                } else {
                    val docId = querySnapshot.documents[0].id
                    atualizarAdmExistente(docId)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun salvarNovoAdm() {
        val dadosAdm = hashMapOf(
            "nomeCompleto" to nomeCompleto.text.toString().trim(),
            "nomeUsuario" to nomeUsuario.text.toString().trim(),
            "email" to email.text.toString().trim(),
            "senha" to senha.text.toString().trim(),
            "cargo" to cargo.text.toString().trim(),
            "contato" to contato.text.toString().trim()
        )

        fb.collection("administradores").add(dadosAdm)
            .addOnSuccessListener {
                Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar os dados.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun atualizarAdmExistente(docId: String) {
        val dadosAtualizados = mapOf(
            "nomeCompleto" to nomeCompleto.text.toString().trim(),
            "nomeUsuario" to nomeUsuario.text.toString().trim(),
            "senha" to senha.text.toString().trim(),
            "cargo" to cargo.text.toString().trim(),
            "contato" to contato.text.toString().trim()
        )

        fb.collection("administradores").document(docId)
            .update(dadosAtualizados)
            .addOnSuccessListener {
                Toast.makeText(this, "Informações atualizadas com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao atualizar os dados.", Toast.LENGTH_SHORT).show()
            }
    }
}

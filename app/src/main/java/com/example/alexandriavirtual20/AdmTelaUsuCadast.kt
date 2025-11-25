package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.UsuarioAdapter
import com.example.alexandriavirtual20.model.Usuario
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AdmTelaUsuCadast : AppCompatActivity() {
    private lateinit var adapter: UsuarioAdapter
    private lateinit var rv: RecyclerView
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnLixeira: ImageButton
    private lateinit var btnAddUsu: Button
    private lateinit var fb: FirebaseFirestore
    private var listenerReg: ListenerRegistration? = null
    private var cache: List<Usuario> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_cadast_usu)

        rv = findViewById(R.id.rvUsuario)
        btnVoltar = findViewById(R.id.botaoVoltar4)
        btnLixeira = findViewById(R.id.btnExcProd3)
        btnAddUsu = findViewById(R.id.addUsu)

        fb = FirebaseFirestore.getInstance()

        rv.layoutManager = LinearLayoutManager(this)

        adapter = UsuarioAdapter(
            onEditar = { usuario ->
                val intent = Intent(this, AdmTelaInfoUsu::class.java)
                intent.putExtra("documentId", usuario.id)
                startActivity(intent)
            }
        )

        rv.setHasFixedSize(true)
        rv.adapter = adapter

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnLixeira.setOnClickListener {
            confirmarExclusao()
        }


        btnAddUsu.setOnClickListener {
            val intent = Intent(this, AdmTelaCadastUsu::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        listenerReg = fb.collection("usuario")
            .orderBy("nome")
            .addSnapshotListener { snaps, e ->
                if (e != null || snaps == null) return@addSnapshotListener

                cache = snaps.documents.map { d ->
                    Usuario(
                        id = d.id,
                        nome = d.getString("nome").orEmpty(),
                        usuario = d.getString("usuario").orEmpty(),
                        email = d.getString("email").orEmpty(),
                        fotoPerfil = d.getString("imgPerfil").orEmpty()
                    )
                }
                adapter.submitList(cache)
            }
    }

    override fun onStop() {
        super.onStop()
        listenerReg?.remove()
        listenerReg = null
    }

    private fun confirmarExclusao(){
        val selecionados = adapter.getSelecionados()

        if (selecionados.isEmpty()) {
            Toast.makeText(this, "Selecione pelo menos um usuário.", Toast.LENGTH_SHORT).show()
        }

        val mensagem = if (selecionados.size == 1) {
            "Tem certeza que deseja excluir o usuário \"${selecionados.first().nome}\"?"
        } else {
            "Tem certeza que deseja excluir ${selecionados.size} usuários?"
        }

        MaterialAlertDialogBuilder(this)
            .setMessage(mensagem)
            .setPositiveButton("Sim") { _, _ -> deletarSelecionados() }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deletarSelecionados(){
        val selecionados = adapter.getSelecionados()

        val batch = fb.batch()
        selecionados.forEach { u ->
            val ref = fb.collection("usuario").document(u.id)
            batch.delete(ref)
        }

        batch.commit()
            .addOnSuccessListener {
                adapter.clearSelecao()
                Toast.makeText(this, "Excluídos com sucesso.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Falha ao excluir.", Toast.LENGTH_SHORT).show()
            }
    }
}

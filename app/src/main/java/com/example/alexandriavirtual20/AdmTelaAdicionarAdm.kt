package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.TornarAdmAdapter
import com.example.alexandriavirtual20.model.Usuario
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class AdmTelaAdicionarAdm : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var fb: FirebaseFirestore
    private lateinit var adapter: TornarAdmAdapter
    private val listaUsuarios = mutableListOf<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_adicionar_adm)
        FirebaseApp.initializeApp(this)

        fb = FirebaseFirestore.getInstance()
        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recyTornarAdm)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TornarAdmAdapter(listaUsuarios) { usuario ->
            tornarAdministrador(usuario)
        }
        recyclerView.adapter = adapter

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        carregarUsuarios()
    }

    private fun carregarUsuarios() {
        fb.collection("usuario")
            .get()
            .addOnSuccessListener { result ->
                listaUsuarios.clear()
                for (document in result) {
                    val usuario = Usuario(
                        id = document.id,
                        nome = document.getString("nome") ?: "",
                        usuario = document.getString("usuario") ?: "",
                        email = document.getString("email") ?: "",
                        fotoPerfil = document.getString("fotoPerfil")
                    )
                    listaUsuarios.add(usuario)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar usuários.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun tornarAdministrador(usuario: Usuario) {
        val dadosAdm = hashMapOf(
            "nome" to usuario.nome,
            "usuario" to usuario.usuario,
            "email" to usuario.email,
            "fotoPerfil" to usuario.fotoPerfil,
            "cargo" to "Administrador"
        )

        val usuariosRef = fb.collection("usuario").document(usuario.id)
        val adminsRef = fb.collection("administradores")

        // Adiciona o usuário na coleção de administradores
        adminsRef.add(dadosAdm)
            .addOnSuccessListener {
                // Depois de adicionar, remove da coleção de usuários
                usuariosRef.delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "${usuario.nome} agora é Administrador!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Remove da lista do RecyclerView também
                        listaUsuarios.remove(usuario)
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Erro ao remover usuário da lista de usuários.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao promover usuário.", Toast.LENGTH_SHORT).show()
            }
    }
    }

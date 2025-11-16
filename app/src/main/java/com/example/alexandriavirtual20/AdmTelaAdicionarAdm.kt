package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
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
    private lateinit var searchView: SearchView
    private lateinit var fb: FirebaseFirestore
    private lateinit var adapter: TornarAdmAdapter

    // Lista completa vinda do Firebase
    private val listaUsuarios = mutableListOf<Usuario>()


    // Lista que aparece na tela (filtrada)
    private val listaFiltrada = mutableListOf<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_adicionar_adm)
        FirebaseApp.initializeApp(this)

        fb = FirebaseFirestore.getInstance()

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recyTornarAdm)
        searchView = findViewById(R.id.pesquisaAddAdm)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TornarAdmAdapter(listaFiltrada) { usuario ->
            tornarAdministrador(usuario)
        }
        recyclerView.adapter = adapter

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        carregarUsuarios()
        configurarBusca()
    }

    private fun carregarUsuarios() {
        fb.collection("usuario")
            .get()
            .addOnSuccessListener { result ->
                listaUsuarios.clear()
                listaFiltrada.clear()

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

                // Inicialmente exibe tudo
                listaFiltrada.addAll(listaUsuarios)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar usuários.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun configurarBusca() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(texto: String?): Boolean {
                filtrarUsuarios(texto ?: "")
                return true
            }
        })
    }

    private fun filtrarUsuarios(query: String) {
        listaFiltrada.clear()

        val filtro = query.lowercase()

        listaFiltrada.addAll(
            listaUsuarios.filter { usuario ->
                usuario.nome.lowercase().contains(filtro) ||
                        usuario.usuario.lowercase().contains(filtro) ||
                        usuario.email.lowercase().contains(filtro)
            }
        )

        adapter.notifyDataSetChanged()
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

        adminsRef.add(dadosAdm)
            .addOnSuccessListener {
                usuariosRef.delete()
                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            "${usuario.nome} agora é Administrador!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Remove das listas
                        listaUsuarios.remove(usuario)
                        listaFiltrada.remove(usuario)

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
    }
}

package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
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

    private lateinit var searchView: SearchView
    private lateinit var fb: FirebaseFirestore
    private var listenerReg: ListenerRegistration? = null

    // 🌟 Mude para MutableList para garantir flexibilidade, embora não seja estritamente necessário
    private var cache: List<Usuario> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_cadast_usu)

        // ... (Inicializações existentes)

        rv = findViewById(R.id.rvUsuario)
        btnVoltar = findViewById(R.id.botaoVoltar4)
        btnLixeira = findViewById(R.id.btnExcProd3)
        btnAddUsu = findViewById(R.id.addUsu)
        searchView = findViewById(R.id.pesquisaAddAdm)

        fb = FirebaseFirestore.getInstance()

        // ... (Configuração do Adapter e RecyclerView)

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

        // ... (Botões de clique existentes)

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

        // 🌟 1. Adicionar o Listener de Busca aqui
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Este método é chamado quando o usuário pressiona Enter ou o ícone de busca
            override fun onQueryTextSubmit(query: String?): Boolean = false

            // Este método é chamado a cada mudança de texto (o que queremos)
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarUsuarios(newText ?: "")
                return true
            }
        })
    }

    override fun onStart() {
        super.onStart()

        listenerReg = fb.collection("usuario")
            .orderBy("nome")
            .addSnapshotListener { snaps, e ->
                if (e != null || snaps == null) return@addSnapshotListener

                // 🌟 Atualiza o cache com todos os usuários não-admin
                cache = snaps.documents
                    .filter { d ->
                        val isAdmin = d.getBoolean("admin") ?: false
                        !isAdmin
                    }
                    .map { d ->
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


    private fun filtrarUsuarios(query: String) {
        val listaFiltrada = if (query.isBlank()) {
            // Se a busca estiver vazia, retorna o cache completo
            cache
        } else {
            // Filtra o cache completo (case-insensitive)
            cache.filter { usuario ->
                usuario.nome.contains(query, ignoreCase = true) ||
                        usuario.usuario.contains(query, ignoreCase = true)
                // Opcional: || usuario.email.contains(query, ignoreCase = true)
            }
        }
        // Atualiza o RecyclerView com a lista filtrada
        adapter.submitList(listaFiltrada)
    }
    private fun confirmarExclusao(){
        // Pega a lista de usuários selecionados do Adapter
        val selecionados = adapter.getSelecionados()

        if (selecionados.isEmpty()) {
            Toast.makeText(this, "Selecione pelo menos um usuário.", Toast.LENGTH_SHORT).show()
            return // ⬅️ Adicionado 'return' para sair da função
        }

        val mensagem = if (selecionados.size == 1) {
            // Usa o nome do primeiro usuário selecionado para a mensagem
            "Tem certeza que deseja excluir o usuário \"${selecionados.first().nome}\"?"
        } else {
            // Mensagem para múltiplos usuários
            "Tem certeza que deseja excluir ${selecionados.size} usuários?"
        }


        MaterialAlertDialogBuilder(this)
            .setMessage(mensagem)
            .setPositiveButton("Sim") { _, _ ->

                deletarSelecionados()
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deletarSelecionados(){
        val selecionados = adapter.getSelecionados()

        if (selecionados.isEmpty()) return

        val batch = fb.batch()


        selecionados.forEach { u ->
            val ref = fb.collection("usuario").document(u.id) // u.id é o Document ID
            batch.delete(ref)
        }

        batch.commit()
            .addOnSuccessListener {

                adapter.clearSelecao()
                Toast.makeText(this, "Usuários excluídos com sucesso.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Falha ao excluir: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
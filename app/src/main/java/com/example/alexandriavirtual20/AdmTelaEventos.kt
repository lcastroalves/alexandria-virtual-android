package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Importação necessária
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.ListaEventoAdmAdapter
import com.example.alexandriavirtual20.model.Evento
import com.google.firebase.firestore.FirebaseFirestore

class AdmTelaEventos : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnExcluir: ImageButton
    private lateinit var btnAdEvento: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    private lateinit var fb: FirebaseFirestore
    private lateinit var adapter: ListaEventoAdmAdapter

    private val cache = mutableListOf<Evento>()
    private val listaFiltrada = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_eventos)

        fb = FirebaseFirestore.getInstance()


        recyclerView = findViewById(R.id.recyListaEventoAdm)
        btnVoltar = findViewById(R.id.botaoVoltar2)
        btnExcluir = findViewById(R.id.botaoExcluirEventos)
        btnAdEvento = findViewById(R.id.botaoAdEvento)
        searchView = findViewById(R.id.searchViewEventos)

        adapter = ListaEventoAdmAdapter(listaFiltrada) { evento ->
            val intent = Intent(this, AdmTelaEditEvent::class.java)
            intent.putExtra("nomeEvento", evento.nome)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        configurarPesquisa()
        observarEventos()

        btnVoltar.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        btnAdEvento.setOnClickListener {
            startActivity(Intent(this, AdmTelaAdicionEvent::class.java))
        }

        // --- MUDANÇA AQUI: Chama o novo método de confirmação ---
        btnExcluir.setOnClickListener {
            confirmarExclusaoEventos()
        }
    }

    private fun configurarPesquisa() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(texto: String?): Boolean {
                filtrarEventos(texto.orEmpty())
                return true
            }
        })
    }

    private fun filtrarEventos(filtro: String) {
        val texto = filtro.lowercase()

        listaFiltrada.clear()
        listaFiltrada.addAll(
            cache.filter { evento ->
                evento.nome.lowercase().contains(texto) ||
                        evento.breveDescricao.lowercase().contains(texto) ||
                        evento.local.lowercase().contains(texto)
            }
        )

        adapter.notifyDataSetChanged()
    }

    private fun atualizarListaFiltrada() {
        listaFiltrada.clear()
        listaFiltrada.addAll(cache)
        adapter.notifyDataSetChanged()
    }

    private fun observarEventos() {
        fb.collection("evento")
            .addSnapshotListener { snapshots, error ->
                if (error != null || snapshots == null) {
                    Toast.makeText(this, "Erro ao carregar eventos.", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                cache.clear()

                for (document in snapshots) {
                    val evento = Evento(
                        imagem = document.getString("imagem") ?: "",
                        nome = document.getString("nome") ?: "",
                        data = document.getString("data") ?: "",
                        horario = document.getString("horario") ?: "",
                        descricao = document.getString("descricao") ?: "",
                        breveDescricao = document.getString("breveDescricao") ?: "",
                        local = document.getString("local") ?: "",
                        isSelected = false
                    )
                    cache.add(evento)
                }

                atualizarListaFiltrada()
            }
    }

    // --- NOVO MÉTODO: Pede confirmação antes de excluir ---
    private fun confirmarExclusaoEventos() {
        val selecionados = listaFiltrada.filter { it.isSelected }

        if (selecionados.isEmpty()) {
            Toast.makeText(this, "Selecione pelo menos um evento.", Toast.LENGTH_SHORT).show()
            return
        }

        val mensagem = if (selecionados.size == 1) {
            "Tem certeza que deseja excluir o evento \"${selecionados[0].nome}\"?"
        } else {
            "Tem certeza que deseja excluir ${selecionados.size} eventos?"
        }

        AlertDialog.Builder(this)
            .setTitle("Confirmação")
            .setMessage(mensagem)
            .setPositiveButton("Sim") { dialog, _ ->
                excluirEventosSelecionados(selecionados)
                dialog.dismiss()
            }
            .setNegativeButton("Não") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    // --- FIM NOVO MÉTODO ---

    private fun excluirEventosSelecionados(eventosSelecionados: List<Evento>) {
        for (evento in eventosSelecionados) {
            fb.collection("evento")
                .whereEqualTo("nome", evento.nome)
                .get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        fb.collection("evento").document(doc.id).delete()
                    }
                }
        }
        // Nota: A lista será atualizada automaticamente via addSnapshotListener (observarEventos)
        Toast.makeText(this, "Evento(s) excluído(s).", Toast.LENGTH_SHORT).show()
    }
}
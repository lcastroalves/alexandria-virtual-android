package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        btnExcluir.setOnClickListener {
            val selecionados = listaFiltrada.filter { it.isSelected }
            if (selecionados.isEmpty()) {
                Toast.makeText(this, "Selecione pelo menos um evento.", Toast.LENGTH_SHORT).show()
            } else {
                confirmarExclusaoEventos(selecionados)
            }
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
                if (error != null || snapshots == null) return@addSnapshotListener

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

    private fun confirmarExclusaoEventos(selecionados: List<Evento>) {
        val mensagem = if (selecionados.size == 1) {
            "Tem certeza que deseja excluir o evento \"${selecionados[0].nome}\"?"
        } else {
            "Tem certeza que deseja excluir ${selecionados.size} eventos?"
        }

        AlertDialog.Builder(this)
            .setTitle("Confirmação")
            .setMessage(mensagem)
            .setPositiveButton("Sim") { dialog, _ ->
                excluirDoFirestore(selecionados)
                dialog.dismiss()
            }
            .setNegativeButton("Não") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun excluirDoFirestore(eventosSelecionados: List<Evento>) {
        val colecao = fb.collection("evento")
        var deletados = 0
        val total = eventosSelecionados.size

        for (evento in eventosSelecionados) {
            colecao.whereEqualTo("nome", evento.nome).get()
                .addOnSuccessListener { query ->
                    for (doc in query) {
                        colecao.document(doc.id).delete()
                            .addOnSuccessListener {
                                deletados++
                                if (deletados == total) {
                                    observarEventos()
                                }
                            }
                    }
                }
        }
    }
}

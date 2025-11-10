package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
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
    private lateinit var fb: FirebaseFirestore
    private lateinit var adapter: ListaEventoAdmAdapter
    private val listaEventos = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_eventos)

        // Inicializações
        fb = FirebaseFirestore.getInstance()
        fb.clearPersistence() // limpa o cache local
        recyclerView = findViewById(R.id.recyListaEventoAdm)
        btnVoltar = findViewById(R.id.botaoVoltar2)
        btnExcluir = findViewById(R.id.botaoExcluirEventos)
        btnAdEvento = findViewById(R.id.botaoAdEvento)

        // Configura o adapter com o clique em um item
        adapter = ListaEventoAdmAdapter(listaEventos) { evento ->
            val intent = Intent(this, AdmTelaEditEvent::class.java)
            intent.putExtra("nomeEvento", evento.nome)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Carrega os eventos em tempo real
        observarEventos()

        // Botão voltar
        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Botão adicionar evento
        btnAdEvento.setOnClickListener {
            startActivity(Intent(this, AdmTelaAdicionEvent::class.java))
        }

        // Botão excluir eventos selecionados
        btnExcluir.setOnClickListener {
            val selecionados = listaEventos.filter { it.isSelected }
            if (selecionados.isEmpty()) {
                Toast.makeText(this, "Selecione pelo menos um evento.", Toast.LENGTH_SHORT).show()
            } else {
                excluirEventosSelecionados(selecionados)
            }
        }
    }

    private fun observarEventos() {
        fb.collection("evento")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(this, "Erro ao carregar eventos.", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                listaEventos.clear()
                for (document in snapshots!!) {
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
                    listaEventos.add(evento)
                }
                adapter.notifyDataSetChanged()
            }
    }

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
        Toast.makeText(this, "Evento(s) excluído(s).", Toast.LENGTH_SHORT).show()
    }
}

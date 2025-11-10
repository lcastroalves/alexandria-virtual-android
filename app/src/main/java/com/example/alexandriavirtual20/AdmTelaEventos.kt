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
private lateinit var adapter: ListaEventoAdmAdapter
private val eventos = mutableListOf<Evento>()


class AdmTelaEventos : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnExcluir: ImageButton
    private lateinit var btnAdEvento: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var fb : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_eventos)

        btnVoltar = findViewById(R.id.botaoVoltar2)
        btnAdEvento = findViewById(R.id.botaoAdEvento)
        btnExcluir = findViewById(R.id.botaoExcluirEventos)
        recyclerView = findViewById(R.id.recyListaEventoAdm)
        fb = FirebaseFirestore.getInstance()

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnAdEvento.setOnClickListener {
            intent = Intent(this, AdmTelaAdicionEvent::class.java)
            startActivity(intent)
        }

        var adapter: ListaEventoAdmAdapter? = null

        adapter = ListaEventoAdmAdapter(carregarEventos()) { evento ->
            val intent = Intent(this, AdmTelaEditEvent::class.java)
            startActivity(intent)
        }

        btnExcluir.setOnClickListener {
            if (adapter.isEmpty()) {
                Toast.makeText(this, "Selecione um item", Toast.LENGTH_SHORT).show()
            }
            else {
                adapter.excluirSelecionados()
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun carregarEventos() : MutableList<Evento> {

        fb.collection("evento")
            .get()
            .addOnSuccessListener { result ->
                eventos.clear()
                for (document in result) {
                    val evento = Evento(
                        imagem = document.getString("imagem").toString(),
                        nome = document.getString("nome") ?: "",
                        data = document.getString("data") ?: "",
                        horario = document.getString("horario") ?: "",
                        descricao = document.getString("descricao") ?: "",
                        breveDescricao = document.getString("breveDescricao") ?: "",
                        local = document.getString("local") ?: "",
                        isSelected = false
                    )
                    eventos.add(evento)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar usuários.", Toast.LENGTH_SHORT).show()
            }

        return eventos
    }
}
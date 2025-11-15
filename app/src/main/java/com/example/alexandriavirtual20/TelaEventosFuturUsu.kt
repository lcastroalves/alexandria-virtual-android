package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.ListaEventoAdapter
import com.example.alexandriavirtual20.model.Evento
import com.google.firebase.firestore.FirebaseFirestore

class TelaEventosFuturUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var recyclerView : RecyclerView
    private lateinit var fb: FirebaseFirestore
    private lateinit var adapter : ListaEventoAdapter
    private val listaEventos = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_eventos_futuros_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recyListaEvento)
        fb = FirebaseFirestore.getInstance()

        adapter = ListaEventoAdapter(listaEventos) { evento ->
            val intent = Intent(this, AdmTelaEditEvent::class.java)
            intent.putExtra("nomeEvento", evento.nome)
            startActivity(intent)
        }

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapter = ListaEventoAdapter(listaEventos) { evento ->
            val intent = Intent(this, TelaInfoEventoUsu::class.java)
            intent.putExtra("nomeEvento", evento.nome)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)  // OPCAO VERTICAL
        recyclerView.adapter = adapter

        observarEventos()
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
}
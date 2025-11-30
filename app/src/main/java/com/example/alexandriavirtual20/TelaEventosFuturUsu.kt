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
import java.text.SimpleDateFormat
import java.util.*

class TelaEventosFuturUsu : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var fb: FirebaseFirestore
    private lateinit var adapter: ListaEventoAdapter
    private val listaEventos = mutableListOf<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_eventos_futuros_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recyListaEvento)
        fb = FirebaseFirestore.getInstance()

        adapter = ListaEventoAdapter(listaEventos) { evento ->
            val intent = Intent(this, TelaInfoEventoUsu::class.java)
            intent.putExtra("nomeEvento", evento.nome)
            startActivity(intent)
        }

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
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

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                for (document in snapshots!!) {

                    val dataStr = document.getString("data") ?: ""
                    var dataConvertida = Date(0)

                    try {
                        dataConvertida = sdf.parse(dataStr) ?: Date(0)
                    } catch (_: Exception) { }

                    val evento = Evento(
                        imagem = document.getString("imagem") ?: "",
                        nome = document.getString("nome") ?: "",
                        data = dataStr,
                        horario = document.getString("horario") ?: "",
                        descricao = document.getString("descricao") ?: "",
                        breveDescricao = document.getString("breveDescricao") ?: "",
                        local = document.getString("local") ?: "",
                        isSelected = false,
                        // Campo auxiliar para ordenação
                        dataOrdenacao = dataConvertida.time
                    )

                    listaEventos.add(evento)
                }

                listaEventos.sortBy { it.dataOrdenacao }

                adapter.notifyDataSetChanged()
            }
    }
}

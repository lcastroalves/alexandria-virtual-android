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

class AdmTelaEventos : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnExcluir: ImageButton
    private lateinit var btnAdEvento: Button
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_eventos)

        btnVoltar = findViewById(R.id.botaoVoltar2)
        btnAdEvento = findViewById(R.id.botaoAdEvento)
        btnExcluir = findViewById(R.id.botaoExcluirEventos)
        recyclerView = findViewById(R.id.recyListaEventoAdm)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnAdEvento.setOnClickListener {
            intent = Intent(this, AdmTelaAdicionEvent::class.java)
            startActivity(intent)
        }

        val eventos = mutableListOf(
            Evento(R.drawable.totoro,"Meu vizinho totoro - ", "16/09/2025", "16 - 17:30", "O filme de animação será reproduzido no auditório das 16 - 17:30", "O filme de animação será reproduzido no auditório das 16 - 17:30", true, "Auditório"),
            Evento(R.drawable.cosmos, "16/09/2025","16/09/2025", "20 - 21", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21",true, "Auditório"),
            Evento(R.drawable.kiki, "23/09/2025", "23/09/2025","16 - 18", "O filme de animação será reproduzido no auditório das 16 - 18", "O filme de animação será reproduzido no auditório das 16 - 18",true, "Auditório")
        )

        var adapter: ListaEventoAdmAdapter? = null

        adapter = ListaEventoAdmAdapter(eventos) { evento ->
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
}
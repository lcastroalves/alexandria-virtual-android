package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.ListaEventoAdapter
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
            Evento("Meu vizinho totoro - ", "16/09/2025", "16 - 17:30", "O filme de animação será reproduzido no auditório das 16 - 17:30", "O filme de animação será reproduzido no auditório das 16 - 17:30",R.drawable.totoro),
            Evento("Palestra Carl Sagan - ", "16/09/2025", "20 - 21", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21",R.drawable.cosmos),
            Evento("O serviço de entregas da kiki - ", "23/09/2025", "16 - 18", "O filme de animação será reproduzido no auditório das 16 - 18", "O filme de animação será reproduzido no auditório das 16 - 18",R.drawable.kiki),
        )

        var adapter: ListaEventoAdapter? = null

        adapter = ListaEventoAdapter(eventos) { evento ->
            val intent = Intent(this, TelaInfoEventoUsu::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
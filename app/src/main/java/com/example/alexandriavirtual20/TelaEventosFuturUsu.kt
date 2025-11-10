package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.ListaEventoAdapter
import com.example.alexandriavirtual20.model.Evento

class TelaEventosFuturUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_eventos_futuros_usu)
        
        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recyListaEvento)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val eventos = mutableListOf(
            Evento((R.drawable.totoro).toString(), "Meu vizinho totoro", "16/09/2025", "16:00 - 17:30", "O filme de animação será reproduzido no auditório das 16 - 17:30", "O filme de animação será reproduzido no auditório das 16 - 17:30", "Auditório Principal",false),
            Evento((R.drawable.cosmos).toString(), "Palestra Carl Sagan","16/09/2025", "20:00 - 21:00", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21", "Auditório",false),
            Evento((R.drawable.kiki).toString(), "O serviço de entregas da Kiki", "23/09/2025","16:00 - 18:00", "O filme de animação será reproduzido no auditório das 16 - 18", "O filme de animação será reproduzido no auditório das 16 - 18", "Auditório",false)
            )

        var adapter: ListaEventoAdapter? = null

        adapter = ListaEventoAdapter(eventos) { evento ->
            val intent = Intent(this, TelaInfoEventoUsu::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)  // OPCAO VERTICAL
        recyclerView.adapter = adapter
    }
}
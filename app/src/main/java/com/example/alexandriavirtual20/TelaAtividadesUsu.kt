package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.AtividadeAdapter
import com.example.alexandriavirtual20.adapter.ListaEventoAdapter
import com.example.alexandriavirtual20.model.Atividade
import com.example.alexandriavirtual20.model.Evento

class TelaAtividadesUsu : AppCompatActivity() {
    private lateinit var btnVoltar : ImageButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_atividades_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recyListaAtividade)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val atividades = mutableListOf(
            Atividade("Cinema", "9 - 22", "Auditório Principal", "A nossa biblioteca oferece um espaço para reprodução de filmes, diversão garantida para toda família!", "A nossa biblioteca oferece um espaço para reprodução de filmes, diversão garantida para toda família!",R.drawable.cinema),
            Atividade("Sala de Jogos", "9 - 22", "Sala 2", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21", "Palestra sobre cosmos, livro de astronomia de Carl Sagan, acontecerá no auditório, das 20 - 21",R.drawable.jogos),
            Atividade("Clube do Livro", "9 - 22", "Auditório Principal", "O filme de animação será reproduzido no auditório das 16 - 18", "O filme de animação será reproduzido no auditório das 16 - 18",R.drawable.iconelivro),
        )

        var adapter: AtividadeAdapter? = null

        adapter = AtividadeAdapter(atividades) { atividade ->
            val intent = Intent(this, TelaInfoAtividadeUsu::class.java)
            intent.putExtra("nomeAtividade", atividade.nome)
            intent.putExtra("horarioAtividade", atividade.horario)
            intent.putExtra("localAtividade", atividade.local)
            intent.putExtra("descricaoAtividade", atividade.descricao)
            intent.putExtra("imagemAtividade", atividade.imagem)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)  // OPCAO VERTICAL
        recyclerView.adapter = adapter
    }
}
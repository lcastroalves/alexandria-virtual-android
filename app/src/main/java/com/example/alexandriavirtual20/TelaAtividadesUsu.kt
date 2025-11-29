package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.AtividadeAdapter
import com.example.alexandriavirtual20.model.Atividade

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
            Atividade("Cinema", "9:00 - 22:00", "Auditório Principal", "A nossa biblioteca oferece um espaço para reprodução de filmes, diversão garantida para toda família!", "A nossa biblioteca oferece um espaço para reprodução de filmes, diversão garantida para toda família!",R.drawable.cinema),
            Atividade("Sala de Jogos", "9:00 - 22:00", "Sala 7", "A sala de jogos é um espaço dedicado ao entretenimento para família e amigos, contando com mais de 50 jogos variados, atendemos a todos os públicos, venha se divertir bastante!", "Nossa biblioteca dispõe de uma sala de jogos cheia de variedade, venha conhecer!",R.drawable.jogos),
            Atividade("Clube do Livro", "12:00 - 19:00", "Sala 1", "O clube do livro é um grupo dos nossos clientes que se reunem para discutir diversas obras, divulgando histórias, conhecimento e experiências, além de ser uma ótima oportunidade para conversar e socializar com outros amantes dos livros, venha participar!", "Nossa biblioteca possui um clube do livro, consulte no balcão como participar!",R.drawable.iconelivro)
        )

        var adapter: AtividadeAdapter? = null

        adapter = AtividadeAdapter(atividades) { atividade ->
            val intent = Intent(this, TelaInfoAtividadeUsu::class.java)
            intent.putExtra("nomeAtividade", atividade.nome)
            intent.putExtra("horarioAtividade", atividade.horario)
            intent.putExtra("imagemAtividade", atividade.imagem)
            intent.putExtra("descricaoAtividade", atividade.descricao)
            intent.putExtra("localAtividade", atividade.local)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
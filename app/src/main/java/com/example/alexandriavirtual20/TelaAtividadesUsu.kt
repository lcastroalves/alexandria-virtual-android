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
            Atividade("Cinema", "9:00 - 22:00", "Auditório Principal", "O auditório principal da nossa biblioteca possui uma prograação de filmes que são reproduzidos regularmente para aumentar o repertório cultural da nossa unidade, fique atento ao que está passando na parte de eventos do nosso aplicativo!", "A nossa biblioteca oferece um espaço para reprodução de filmes, diversão garantida para toda família!",R.drawable.cinema),
            Atividade("Sala de Jogos", "9:00 - 22:00", "Sala 7", "A nossa biblioteca dispõe de uma sala de jogos com uma variedade de jogos de tabuleiro e cartas, que atendem desde o público infantil até os mais velhos, temos Uno, baralho, xadrez e diversas outras opções!", "A nossa biblioteca oferece um espaço para jogos de cartas e tabuleiro, venha jogar com sua família e amigos!",R.drawable.jogos),
            Atividade("Clube do Livro", "9:00 - 22:00", "Sala 1", "A nossa biblioteca possui um clube do livro, em que nossos clientes abordam diversas obras a fundo, dando opiniões, referências, indicações e debatendo, uma experiência envolvendo amantes de livros perfeita para conversar e socializar! Confira no balcão o processo de inscrição para o clube!", "Nossa biblioteca possui um Clube do Livro. Consulte o balcão para saber como se inscrever!",R.drawable.iconelivro),
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
package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.Notificacao
import com.example.alexandriavirtual20.adapter.NotificacaoAdapter


class TelaNotificacoesUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_notificacoes_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recy)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val notificacoes = mutableListOf(
            Notificacao("Ciência da computação - ", "Devolução", "16 set", "Falta uma semana para fim do prazo de devolução", R.drawable.livro1),
            Notificacao("Ciência da computação - ", "Devolução", "16 set", "Falta uma semana para fim do prazo de devolução", R.drawable.livro2),
            Notificacao("Ciência da computação - ", "Devolução", "16 set", "Falta uma semana para fim do prazo de devolução", R.drawable.livro3),
            Notificacao("Ciência da computação - ", "Devolução", "16 set", "Falta uma semana para fim do prazo de devolução", R.drawable.livro4),

        )

        var adapter: NotificacaoAdapter? = null

        // Cria o Adapter e envia a lista produtos para ele
        adapter = NotificacaoAdapter(
            notificacoes,
            onExcluirClick = { notificacao ->
                adapter?.removerNotificacao(notificacao)
            }
            )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}

//A tela de notificações deverá receber informações de tempo, através de atributos de outros objetos, como prazo de devolução de um livro ou data de eventos (eventos).
//A tela de notificações deverá exibir as notificações por ordem de proximidade de data
//A tela de notificações deverá exibir 3 tipos de dados: O evento é hoje / faltam (1 - 6) dias / faltam x semana(s) para o evento
//A tela de notificações deverá exibir imagem do evento/título do evento/data do evento/um x para remoção do evento
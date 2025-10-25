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
            Notificacao("Ciência da computação - ", "Devolução", "16 set", "Falta uma semana para fim do prazo de devolução",  1,R.drawable.livro1),
            Notificacao("Meu vizinho totoro - ", "Evento", "16 set", "Falta uma semana para realização do evento", 1,R.drawable.totoro),
            Notificacao("Java como programar - ", "Devolução", "23 set", "Faltam duas semanas para fim do prazo de devolução",2, R.drawable.livro3),
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

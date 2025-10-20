package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaNotificacoesUsu : AppCompatActivity() {
    private lateinit var btnVoltar: Button
    private lateinit var btnApagar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_notificacoes_usu)
    }
}

//A tela de notificações deverá receber informações de tempo, através de atributos de outros objetos, como prazo de devolução de um livro ou data de eventos (eventos).
//A tela de notificações deverá exibir as notificações por ordem de proximidade de data
//A tela de notificações deverá exibir 3 tipos de dados: O evento é hoje / faltam (1 - 6) dias / faltam x semana(s) para o evento
//A tela de notificações deverá exibir imagem do evento/título do evento/data do evento/um x para remoção do evento
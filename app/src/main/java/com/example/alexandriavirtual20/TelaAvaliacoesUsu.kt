package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.ComentarioAdapter
import com.example.alexandriavirtual20.model.Comentario

class TelaAvaliacoesUsu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_avaliacoes_usu)

        val btnvoltar: ImageButton = findViewById(R.id.btnvoltar)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerAvaliacoes)


        val comentarios = listOf(
            Comentario(
                "Felipe Barroso",
                "Java é uma péssima linguagem pra aprender, não vou nem tentar 😅",
                1,
                R.drawable.imagem_do_usuario_com_fundo_preto
            ),
            Comentario(
                "Nicolly Feitosa",
                "Amei esse livro, fantástico! 😍",
                5,
                R.drawable.imagem_do_usuario_com_fundo_preto
            ),
            Comentario(
                "Lara Castro",
                "Muito bom pra revisar conceitos básicos, eu adorei de verdade e vou recomendar para meus amigos programadores!",
                4,
                R.drawable.imagem_do_usuario_com_fundo_preto
            ),
            Comentario(
                "Dillan Medeiros",
                "Achei legalzinho o livro, interessante para aprender conceitos de back-end, mas prefiro o front e sou do time do HTML/CSS e Javascript hehehe",
                3,
                R.drawable.imagem_do_usuario_com_fundo_preto
            ),
            Comentario(
                "Jonh Henrique",
                "A man, achei bem ok sabe, eu gosto de java mas fiquei muito na dúvida sobre esse livro, mas gostei de alguns assuntos",
                2,
                R.drawable.imagem_do_usuario_com_fundo_preto
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ComentarioAdapter(comentarios)

        btnvoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}

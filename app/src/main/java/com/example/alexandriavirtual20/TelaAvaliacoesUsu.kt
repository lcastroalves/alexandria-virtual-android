package com.example.alexandriavirtual20

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast // Importe o Toast para feedback de erro
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.ComentarioAdapter
import com.example.alexandriavirtual20.model.Comentario
import com.example.alexandriavirtual20.model.Livro
import com.google.firebase.firestore.FirebaseFirestore

class TelaAvaliacoesUsu : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ComentarioAdapter
    private val listaComentarios = mutableListOf<Comentario>()

    // Variáveis de classe não mais necessárias foram removidas (subtitulo, autorLivroSuperior, etc.)
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_avaliacoes_usu)

        // Recebe o objeto Livro via Intent
        val livro = intent.getParcelableExtra<Livro>("livro")

        if (livro == null || livro.id.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: Informações do livro ausentes.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ---------- Componentes da interface (Inicialização Local) ----------
        val btnVoltar: ImageButton = findViewById(R.id.btnvoltar)
        val imgLivro: ImageView = findViewById(R.id.imageView16)
        val txtTitulo: TextView = findViewById(R.id.txtTituloAvaliacoes)
        val txtAutor: TextView = findViewById(R.id.txtAutorLivro)


        // ---------- Configuração do RecyclerView de Comentários ----------
        recyclerView = findViewById(R.id.recyclerAvaliacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ComentarioAdapter(listaComentarios)
        recyclerView.adapter = adapter

        // ---------- Exibe dados do livro ----------
        preencherInfos(livro, imgLivro, txtTitulo, txtAutor)

        // ---------- Carrega avaliações do Firebase ----------
        carregarAvaliacoes(livro.id)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // Novo método para organizar o preenchimento da UI
    private fun preencherInfos(
        livro: Livro,
        imgView: ImageView,
        txtTitle: TextView,
        txtAuthor: TextView
    ) {
        // Carrega a capa usando Base64
        if (!livro.capa.isNullOrEmpty()) {
            try {
                val imageBytes = Base64.decode(livro.capa, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imgView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // Opcional: Definir uma imagem padrão aqui em caso de falha na decodificação
                // imgView.setImageResource(R.drawable.no_image_placeholder)
            }
        }

        txtTitle.text = livro.titulo ?: "Título Não Informado"
        txtAuthor.text = livro.autor ?: "Autor Desconhecido"

        // Se precisar usar a variável 'avaliacoes' para algo:
        // avaliacoes.text = "Média de Avaliações: X.X"
    }

    private fun carregarAvaliacoes(idLivro: String) {

        db.collection("livros")
            .document(idLivro)
            .collection("comentarios")
            .get()
            .addOnSuccessListener { resultado ->

                listaComentarios.clear()

                for (doc in resultado) {
                    val comentario = doc.toObject(Comentario::class.java)
                    // Garante que Comentario é uma data class válida e tem construtor vazio se usando toObject
                    listaComentarios.add(comentario)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Tratar falha no carregamento dos comentários
                Toast.makeText(this, "Erro ao carregar avaliações.", Toast.LENGTH_SHORT).show()
            }
    }
}
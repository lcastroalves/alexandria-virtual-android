package com.example.alexandriavirtual20

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

    private lateinit var imagemLivro: ImageView
    private lateinit var tituloLivro: TextView
    private lateinit var subtitulo: TextView
    private lateinit var autorLivroSuperior: TextView

    private lateinit var avaliacoes: TextView


    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_avaliacoes_usu)

        val livro = intent.getParcelableExtra<Livro>("livro")

        if (livro == null) {
            finish()
            return
        }

        // ---------- Componentes da interface ----------
        val btnVoltar: ImageButton = findViewById(R.id.btnvoltar)
        val imgLivro: ImageView = findViewById(R.id.imageView16)
        val txtTitulo: TextView = findViewById(R.id.txtTituloAvaliacoes)
        val txtAutor: TextView = findViewById(R.id.txtAutorLivro)
        val avaliacoes: TextView = findViewById(R.id.txtInfoAvaliacoes)

        recyclerView = findViewById(R.id.recyclerAvaliacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ComentarioAdapter(listaComentarios)
        recyclerView.adapter = adapter

        // ---------- Exibe dados do livro ----------
        if (!livro.capa.isNullOrEmpty()) {
            val imageBytes = android.util.Base64.decode(livro.capa, android.util.Base64.DEFAULT)
            val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imgLivro.setImageBitmap(bitmap)
        }
        txtTitulo.text = livro.titulo
        txtAutor.text = livro.autor

        // ---------- Carrega avaliações do Firebase ----------
        carregarAvaliacoes(livro.id)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
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
                    listaComentarios.add(comentario)
                }

                adapter.notifyDataSetChanged()
            }
    }
    private fun preencherInfos(livro: Livro) {

        // Se a capa vier como base64
        if (!livro.capa.isNullOrEmpty()) {
            val decodedBytes = Base64.decode(livro.capa, Base64.DEFAULT)
            val bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            imagemLivro.setImageBitmap(bmp)
        }

        tituloLivro.text = livro.titulo ?: "Sem título"
        subtitulo.text = livro.subtitulo ?: ""
        autorLivroSuperior.text = livro.autor ?: "Autor desconhecido"

    }
}

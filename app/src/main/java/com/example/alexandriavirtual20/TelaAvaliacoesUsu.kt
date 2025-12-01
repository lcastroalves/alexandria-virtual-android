package com.example.alexandriavirtual20

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.ComentarioAdapter
import com.example.alexandriavirtual20.model.Comentario
import com.example.alexandriavirtual20.model.Livro
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.DecimalFormat

class TelaAvaliacoesUsu : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ComentarioAdapter
    private val listaComentarios = mutableListOf<Comentario>()
    private val db = FirebaseFirestore.getInstance()

    // Variável para gerenciar a conexão em tempo real
    private var comentariosListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_avaliacoes_usu)

        val livro = intent.getParcelableExtra<Livro>("livro")

        if (livro == null || livro.id.isNullOrEmpty()) {
            finish()
            return
        }

        // ---------- Componentes da interface (Inicialização Local) ----------
        val btnVoltar: ImageButton = findViewById(R.id.btnvoltar)
        val imgLivro: ImageView = findViewById(R.id.imageView16)
        val txtTituloLivro: TextView = findViewById(R.id.txtNomeLivro) // Nome mais claro!
        val txtAutor: TextView = findViewById(R.id.txtAutorLivro)
        val txtNotaGeral: TextView = findViewById(R.id.txtInfoAvaliacoes)


        // ---------- Configuração do RecyclerView de Comentários ----------
        recyclerView = findViewById(R.id.recyclerAvaliacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ComentarioAdapter(listaComentarios)
        recyclerView.adapter = adapter

        // ---------- Exibe dados do livro e média geral ----------
        preencherInfos(livro, imgLivro, txtTituloLivro, txtAutor, txtNotaGeral)

        // ---------- Carrega avaliações do Firebase com Listener ----------
        carregarAvaliacoes(livro.id)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Importante: Remove o listener para evitar vazamento de memória e chamadas desnecessárias
        comentariosListener?.remove()
    }

    private fun preencherInfos(
        livro: Livro,
        imgView: ImageView,
        txtTitle: TextView, // Este parâmetro agora é R.id.txtNomeLivro
        txtAuthor: TextView,
        txtNotaGeral: TextView
    ) {
        // Carrega a capa usando Base64
        if (!livro.capa.isNullOrEmpty()) {
            try {
                val imageBytes = Base64.decode(livro.capa, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imgView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // imgView.setImageResource(R.drawable.no_image_placeholder)
            }
        }

        txtTitle.text = livro.titulo ?: "Título Não Informado" // O titulo do livro será exibido aqui
        txtAuthor.text = livro.autor ?: "Autor Desconhecido"

        // 🌟 EXIBE A NOTA GERAL DO LIVRO RECEBIDA VIA INTENT (campos mediaAvaliacao e totalAvaliacoes)
        if (livro.totalAvaliacoes > 0) {
            val decimalFormat = DecimalFormat("#.#")
            val mediaFormatada = decimalFormat.format(livro.mediaAvaliacao)
            txtNotaGeral.text = "$mediaFormatada ★ (${livro.totalAvaliacoes} avaliações)"
        } else {
            txtNotaGeral.text = "Sem avaliações"
        }
    }

    private fun carregarAvaliacoes(idLivro: String) {

        // 🌟 MUDA DE GET() PARA ADDSNAPSHOTLISTENER()
        comentariosListener = db.collection("livros")
            .document(idLivro)
            .collection("comentarios")
            .addSnapshotListener { resultado, e ->

                if (e != null) {
                    return@addSnapshotListener
                }

                if (resultado != null) {
                    listaComentarios.clear()

                    for (doc in resultado.documents) {
                        // Certifique-se que o modelo Comentario tem um construtor vazio para toObject
                        val comentario = doc.toObject(Comentario::class.java)
                        if (comentario != null) {
                            listaComentarios.add(comentario)
                        }
                    }

                    adapter.notifyDataSetChanged()
                }
            }
    }
}
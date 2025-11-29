package com.example.alexandriavirtual20

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import com.example.alexandriavirtual20.model.Livro
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class TelaInfoLivroUsu : AppCompatActivity  () {

    private lateinit var btnVoltar : ImageButton
    private lateinit var imagemLivro: ImageView
    private lateinit var tituloLivro: TextView
    private lateinit var subtitulo: TextView
    private lateinit var autorLivroSuperior: TextView
    private lateinit var sinopseLivro: TextView
    private lateinit var autorLivroInferior: TextView
    private lateinit var idioma: TextView
    private lateinit var edicao: TextView
    private lateinit var generoLivro: TextView

    private lateinit var fireBase: FirebaseFirestore
    private lateinit var fbAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_info_livro_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        imagemLivro = findViewById(R.id.iconAdm)
        tituloLivro = findViewById(R.id.tituloLivro)
        subtitulo = findViewById(R.id.subtitulo)
        autorLivroSuperior = findViewById(R.id.autorLivroSuperior)
        sinopseLivro = findViewById(R.id.sinopseLivro)
        autorLivroInferior = findViewById(R.id.autorLivroInferior)
        idioma = findViewById(R.id.idioma)
        edicao = findViewById(R.id.edicao)
        generoLivro = findViewById(R.id.generoLivro)

        fireBase = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

        val livroId = intent.getStringExtra("livroId")

        Firebase.firestore.collection("livros")
            .document(livroId!!)
            .get()
            .addOnSuccessListener { doc ->

                val livro = doc.toObject(Livro::class.java)

                if (livro != null) {
                    livro.id = doc.id
                    preencherInfos(livro)
                    salvarNoHistorico(livro)
                }
            }


        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
        autorLivroInferior.text = livro.autor ?: ""
        sinopseLivro.text = livro.sinopse ?: "Nenhuma sinopse disponível"
        idioma.text = livro.idioma ?: "Português"
        edicao.text = livro.edicao ?: "Sem edição"
        generoLivro.text = livro.genero ?: "Não informado"
    }

    private fun salvarNoHistorico(livro: Livro) {

        val usuarioId = fbAuth.currentUser?.uid

        if (usuarioId == null) {
            return
        }

        val historicoData = hashMapOf(
            "id" to livro.id,
            "dataVisualizacao" to System.currentTimeMillis()
        )

        fireBase.collection("usuario")
            .document(usuarioId)
            .collection("historico")
            .document(livro.id)
            .set(historicoData)
            .addOnSuccessListener {
                // salvo.
            }
    }

}
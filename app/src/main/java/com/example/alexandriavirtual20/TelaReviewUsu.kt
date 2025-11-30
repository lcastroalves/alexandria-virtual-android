package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroReviewAdapter
import com.example.alexandriavirtual20.model.Livro
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.round
import com.google.firebase.auth.FirebaseAuth

class TelaReviewUsu : AppCompatActivity() {

    private val TAG = "TelaReviewUsu"

    // --- Variáveis de Estado por Livro (Chave: Livro ID) ---
    private val avaliacoesEstrelas = mutableMapOf<String, Int>()
    private val avaliacoesTexto = mutableMapOf<String, String>()

    private var livroVisivelId: String? = null // ID do livro no centro
    private var listaLivros: List<Livro> = emptyList()

    private val fbAuth = FirebaseAuth.getInstance()

    // --- Componentes da UI ---
    private lateinit var estrelas: List<ImageView>
    private lateinit var editAvaliacao: EditText
    private lateinit var btnEnviar: Button
    // REMOVIDO: private lateinit var txtNotaGeral: TextView <--- Removido
    private lateinit var recyclerLivros: RecyclerView

    private val db = FirebaseFirestore.getInstance()
    private val notasGeraisLivros = mutableMapOf<String, Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_review_usu)

        // Inicializações de UI
        recyclerLivros = findViewById(R.id.recyclerLivros)
        val btnBack: ImageButton = findViewById(R.id.btnback)
        editAvaliacao = findViewById(R.id.editAvaliacao)
        btnEnviar = findViewById(R.id.btnEnviar)
        // REMOVIDO: txtNotaGeral = findViewById(R.id.txtNotaGeral) <--- Removido

        // Estrelas
        estrelas = listOf(
            findViewById(R.id.estrela1), findViewById(R.id.estrela2),
            findViewById(R.id.estrela3), findViewById(R.id.estrela4),
            findViewById(R.id.estrela5)
        )

        carregarLivros()

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        btnEnviar.setOnClickListener { enviarAvaliacao() }
    }

    private fun carregarLivros() {
        db.collection("livros")
            .get()
            .addOnSuccessListener { query ->

                val novaLista = query.documents.map { doc ->
                    Livro(
                        id = doc.id,
                        titulo = doc.getString("titulo") ?: "Título Desconhecido",
                        autor = doc.getString("autor") ?: "Autor Desconhecido",
                        capa = doc.getString("capa") ?: "",
                        genero = doc.getString("genero") ?: "",
                        anoLancamento = doc.getString("anoLancamento") ?: ""
                    )
                }

                if (novaLista.isNotEmpty()) {
                    listaLivros = novaLista
                    configurarRecyclerView()
                } else {
                    Toast.makeText(this, "Nenhum livro encontrado para avaliar.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Erro ao carregar livros: ${e.message}", e)
                Toast.makeText(this, "Falha ao buscar livros para avaliação.", Toast.LENGTH_LONG).show()
            }
    }


    private fun configurarRecyclerView() {

        recyclerLivros.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerLivros.adapter = LivroReviewAdapter(listaLivros)

        PagerSnapHelper().attachToRecyclerView(recyclerLivros)

        configurarScrollListener()
        configurarEstrelasListener()

        livroVisivelId = listaLivros.firstOrNull()?.id
        if (livroVisivelId != null) {
            atualizarUI(0)
        }
    }

    private fun configurarScrollListener() {
        recyclerLivros.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstVisibleItemPosition()

                    if (position != RecyclerView.NO_POSITION && listaLivros[position].id != livroVisivelId) {
                        salvarEstadoAntigo()
                        atualizarUI(position)
                    }
                }
            }
        })
    }

    private fun salvarEstadoAntigo() {
        livroVisivelId?.let { id ->

            // 🌟 CORREÇÃO: Conta quantas estrelas estão com a tag "full"
            val notaAtual = estrelas.count { it.tag == "full" }

            avaliacoesEstrelas[id] = notaAtual
            avaliacoesTexto[id] = editAvaliacao.text.toString()
        }
    }

    private fun atualizarUI(position: Int) {
        val novoLivro = listaLivros[position]
        livroVisivelId = novoLivro.id

        val nota = avaliacoesEstrelas[livroVisivelId] ?: 0
        val texto = avaliacoesTexto[livroVisivelId] ?: ""

        atualizarEstrelas(nota)
        editAvaliacao.setText(texto)


    }

    private fun configurarEstrelasListener() {
        estrelas.forEachIndexed { index, estrela ->
            estrela.setOnClickListener {
                val novaNota = index + 1
                atualizarEstrelas(novaNota)
            }
        }
    }

    private fun atualizarEstrelas(nota: Int) {
        estrelas.forEachIndexed { index, estrela ->
            if (index < nota) {
                estrela.setImageResource(R.drawable.ic_star_full)
                estrela.tag = "full"
            } else {
                estrela.setImageResource(R.drawable.ic_star_border)
                estrela.tag = "border"
            }
        }
    }



    // ... (código anterior da TelaReviewUsu.kt)

    // ... (código anterior)

    private fun enviarAvaliacao() {
        salvarEstadoAntigo()
        val idLivro = livroVisivelId
        val texto = avaliacoesTexto[idLivro]
        val nota = avaliacoesEstrelas[idLivro]

        val userId = fbAuth.currentUser?.uid // Obtém o UID do usuário logado

        if (idLivro == null || nota == null || nota == 0 || texto.isNullOrBlank() || userId.isNullOrBlank()) {
            Toast.makeText(this, "Selecione a nota e escreva sua avaliação", Toast.LENGTH_SHORT).show()
            return
        }

        btnEnviar.isEnabled = false // Desabilitar o botão

        // 🌟 PASSO 1: Buscar o Nome do Usuário
        db.collection("usuario").document(userId).get()
            .addOnSuccessListener { userDoc ->

                val nomeUsuario = userDoc.getString("nome") ?: "Usuário Desconhecido"


                realizarTransacao(idLivro, nota, texto, userId, nomeUsuario)

            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Erro ao buscar nome do usuário: ${e.message}")

                realizarTransacao(idLivro, nota, texto, userId, "Usuário (Falha ao buscar)")
            }
    }

    // 🌟 NOVA FUNÇÃO PARA EXECUTAR A TRANSAÇÃO APÓS OBTER O NOME
    private fun realizarTransacao(
        idLivro: String,
        nota: Int,
        texto: String,
        userId: String,
        nomeUsuario: String
    ) {
        db.runTransaction { transaction ->

            val livroRef = db.collection("livros").document(idLivro)
            val snapshot = transaction.get(livroRef)

            // Cálculo da Média (Logica correta, mantida)
            val mediaAntiga = snapshot.getDouble("mediaAvaliacao") ?: 0.0
            val totalAntigo = snapshot.getLong("totalAvaliacoes") ?: 0L
            val novoTotal = totalAntigo + 1
            val novaSoma = (mediaAntiga * totalAntigo) + nota
            val novaMedia = novaSoma / novoTotal

            // 1. Atualizar o documento principal do livro
            transaction.update(livroRef,
                mapOf(
                    "mediaAvaliacao" to novaMedia,
                    "totalAvaliacoes" to novoTotal
                )
            )

            // 2. Criar os dados do comentário
            val comentarioData = hashMapOf(
                "idUsu" to userId,
                "nomeUsuario" to nomeUsuario,
                "estrelas" to nota, // 🌟 Campo precisa ser "estrelas" (não "rating")
                "comentario" to texto, // 🌟 Campo precisa ser "comentario" (não "reviewText")
                "timestamp" to System.currentTimeMillis()
            )

            // Retorna os dados para serem usados no onSuccess
            return@runTransaction comentarioData
        }.addOnSuccessListener { comentarioData ->

            // Adicione o comentário na subcoleção APÓS a transação ser bem-sucedida.
            db.collection("livros").document(idLivro)
                .collection("comentarios").add(comentarioData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Avaliação enviada e média atualizada!", Toast.LENGTH_LONG).show()

                    // Limpar e atualizar UI
                    avaliacoesEstrelas.remove(idLivro)
                    avaliacoesTexto.remove(idLivro)
                    editAvaliacao.text.clear()
                    atualizarEstrelas(0)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Erro ao adicionar comentário: ${e.message}")
                    Toast.makeText(this, "Erro ao salvar comentário. Média atualizada.", Toast.LENGTH_SHORT).show()
                }

        }.addOnFailureListener { e ->
            Log.e(TAG, "Erro na transação de avaliação: ${e.message}")
            Toast.makeText(this, "Erro ao enviar avaliação. Tente novamente.", Toast.LENGTH_SHORT).show()
        }.addOnCompleteListener {
            btnEnviar.isEnabled = true // Reabilitar o botão
        }
    }
}
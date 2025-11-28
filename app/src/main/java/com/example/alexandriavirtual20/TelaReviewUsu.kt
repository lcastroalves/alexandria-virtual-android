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

class TelaReviewUsu : AppCompatActivity() {

    private val TAG = "TelaReviewUsu"

    // --- Variáveis de Estado por Livro (Chave: Livro ID) ---
    private val avaliacoesEstrelas = mutableMapOf<String, Int>()
    private val avaliacoesTexto = mutableMapOf<String, String>()

    private var livroVisivelId: String? = null // ID do livro no centro

    // Lista para receber os dados do Firebase
    private var listaLivros: List<Livro> = emptyList()

    // --- Componentes da UI ---
    private lateinit var estrelas: List<ImageView>
    private lateinit var editAvaliacao: EditText
    private lateinit var btnEnviar: Button
    private lateinit var txtNotaGeral: TextView
    private lateinit var recyclerLivros: RecyclerView

    private val db = FirebaseFirestore.getInstance()
    private val notasGeraisLivros = mutableMapOf<String, Double>() // Simulação da nota média

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_review_usu)

        // Inicializações de UI
        recyclerLivros = findViewById(R.id.recyclerLivros)
        val btnBack: ImageButton = findViewById(R.id.btnback)
        editAvaliacao = findViewById(R.id.editAvaliacao)
        btnEnviar = findViewById(R.id.btnEnviar)
        txtNotaGeral = findViewById(R.id.txtNotaGeral)

        // Estrelas
        estrelas = listOf(
            findViewById(R.id.estrela1), findViewById(R.id.estrela2),
            findViewById(R.id.estrela3), findViewById(R.id.estrela4),
            findViewById(R.id.estrela5)
        )

        // --- CHAMADA PRINCIPAL ---
        carregarLivros()

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        btnEnviar.setOnClickListener { enviarAvaliacao() }
    }

    // 📚 FUNÇÃO DE CARREGAMENTO DO FIREBASE
    private fun carregarLivros() {
        db.collection("livros")
            .get()
            .addOnSuccessListener { query ->

                // Mapeia os documentos para o modelo Livro
                val novaLista = query.documents.map { doc ->
                    Livro(
                        id = doc.id,
                        titulo = doc.getString("titulo") ?: "Título Desconhecido",
                        autor = doc.getString("autor") ?: "Autor Desconhecido",
                        capa = doc.getString("capa") ?: "",
                        // Mapeie outros campos necessários (genero, anoLancamento, etc.)
                        genero = doc.getString("genero") ?: "",
                        anoLancamento = doc.getString("anoLancamento") ?: ""
                    )
                }

                if (novaLista.isNotEmpty()) {
                    listaLivros = novaLista
                    configurarRecyclerView() // Configura a lista APÓS o carregamento
                } else {
                    Toast.makeText(this, "Nenhum livro encontrado para avaliar.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Erro ao carregar livros: ${e.message}", e)
                Toast.makeText(this, "Falha ao buscar livros para avaliação.", Toast.LENGTH_LONG).show()
            }
    }

    // ⚙️ FUNÇÃO DE CONFIGURAÇÃO DA LISTA E LISTENERS
    private fun configurarRecyclerView() {

        recyclerLivros.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Usa a lista carregada do Firebase
        recyclerLivros.adapter = LivroReviewAdapter(listaLivros)

        PagerSnapHelper().attachToRecyclerView(recyclerLivros)

        // Configurações que dependem da lista estar populada
        configurarScrollListener()
        configurarEstrelasListener()

        // Inicializa a UI com o primeiro livro (se houver)
        livroVisivelId = listaLivros.firstOrNull()?.id
        if (livroVisivelId != null) {
            atualizarUI(0)
        }
    }

    // ---------------------------------------------------------------------------------------
    // *** FUNÇÕES DE GERENCIAMENTO DE ESTADO E UI (SEM ALTERAÇÕES NAS ÚLTIMAS CORREÇÕES) ***
    // ---------------------------------------------------------------------------------------

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
            // Assume que 'estrelas' tem uma tag para identificar o estado, ou conta as cheias
            avaliacoesEstrelas[id] = estrelas.indexOfFirst { it.tag == "full" } + 1
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

        // Simulação: se tiver uma média para este livro, exibe.
        val mediaGeral = notasGeraisLivros[livroVisivelId] ?: 0.0
        atualizarNotaGeral(mediaGeral)
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

    private fun atualizarNotaGeral(media: Double) {
        val mediaArredondada = round(media * 10) / 10
        txtNotaGeral.text = "$mediaArredondada ★★★★★"
    }

    private fun enviarAvaliacao() {
        salvarEstadoAntigo()
        val idLivro = livroVisivelId
        val texto = avaliacoesTexto[idLivro]
        val nota = avaliacoesEstrelas[idLivro]

        if (idLivro == null || nota == null || nota == 0 || texto.isNullOrBlank()) {
            Toast.makeText(this, "Selecione a nota e escreva sua avaliação!", Toast.LENGTH_SHORT).show()
            return
        }

        // Lógica de envio ao Firebase:
        val avaliacaoData = hashMapOf(
            "userId" to "ID_DO_USUARIO_LOGADO",
            "rating" to nota,
            "reviewText" to texto,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("livros")
            .document(idLivro)
            .collection("comentarios")
            .add(avaliacaoData)
            .addOnSuccessListener {
                Toast.makeText(this, "Avaliação enviada para ${idLivro}!", Toast.LENGTH_LONG).show()

                // Limpar o estado APENAS deste livro
                avaliacoesEstrelas.remove(idLivro)
                avaliacoesTexto.remove(idLivro)

                // Resetar a UI
                editAvaliacao.text.clear()
                atualizarEstrelas(0)
            }
            .addOnFailureListener {
                Log.e(TAG, "Erro ao enviar avaliação: ${it.message}")
                Toast.makeText(this, "Erro ao enviar avaliação. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
    }
}
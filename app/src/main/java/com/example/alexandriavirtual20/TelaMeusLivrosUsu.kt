package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast // Adicionado para exibir mensagens de erro
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroAdapterMeus
import com.example.alexandriavirtual20.model.Livro
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration // Para gerenciar o listener

class TelaMeusLivrosUsu : AppCompatActivity() {

    private val TAG = "MeusLivrosUsu"

    private val listaOriginal = mutableListOf<Livro>()
    private val listaFiltrada = mutableListOf<Livro>()
    private lateinit var adapter: LivroAdapterMeus

    private lateinit var firestore: FirebaseFirestore
    // Variável para manter a referência do listener em tempo real
    private var firestoreListener: ListenerRegistration? = null

    private lateinit var txtProgresso: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_meus_livros_usu)

        // 1. Inicializações e Referências de UI
        val btnBack: ImageButton = findViewById(R.id.btnback)
        val btnAvaliar: Button = findViewById(R.id.btnAvaliarLivros)
        txtProgresso = findViewById(R.id.txtProgresso)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerLivros)

        val filtroAlugados: TextView = findViewById(R.id.filtroAlugados)
        val filtroLidos: TextView = findViewById(R.id.filtroLidos)
        val filtroAZ: TextView = findViewById(R.id.filtroAZ)

        firestore = FirebaseFirestore.getInstance()

        btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // 2. Configura Adapter
        adapter = LivroAdapterMeus(listaFiltrada) { livro ->
            // Você precisará garantir que seu modelo Livro tenha um 'id'
            val idLivro = livro.id ?: livro.titulo // Usando título como fallback se 'id' for null
            val intent = Intent(this, TelaInfoLivroUsu::class.java)
            intent.putExtra("livroId", idLivro)
            startActivity(intent)
        }

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        // 3. Carrega os livros do Firestore
        carregarLivros()

        // 4. Configura Filtros
        filtroAlugados.setOnClickListener {
            aplicarFiltro("alugados")
        }

        filtroLidos.setOnClickListener {
            aplicarFiltro("lidos")
        }

        filtroAZ.setOnClickListener {
            aplicarFiltro("az")
        }

        btnAvaliar.setOnClickListener {
            startActivity(Intent(this, TelaReviewUsu::class.java))
        }
    }

    /**
     * Carrega a lista de livros do Firebase em tempo real (Listener).
     * * NOTA: Para um app de usuário, você idealmente filtraria esta lista
     * pela coleção 'emprestimos' ou um campo 'idUsuario' no livro.
     * Aqui, estamos buscando todos os livros da coleção 'livros' por semelhança com a tela Admin.
     */
    private fun carregarLivros() {
        // Remove o listener anterior, se houver
        firestoreListener?.remove()

        // Adiciona um novo listener em tempo real
        firestoreListener = firestore.collection("livros")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Erro ao ouvir snapshots de livros: ", e)
                    Toast.makeText(this, "Erro ao carregar livros.", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    listaOriginal.clear()

                    // Mapeia os documentos para o modelo Livro
                    for (doc in snapshots.documents) {
                        // Certifique-se de que o Livro está sendo mapeado com o ID
                        val livro = Livro(
                            id = doc.id, // O ID do documento é crucial para telas de detalhes/avaliação
                            titulo = doc.getString("titulo") ?: "",
                            autor = doc.getString("autor") ?: "",
                            capa = doc.getString("capa") ?: "",
                            anoLancamento = doc.getString("anoLancamento")?: ""
                            // Adicione outros campos necessários
                            // ex: anoLancamento = doc.getString("anoLancamento") ?: ""
                            // Note: Você pode precisar ajustar a chamada 'Livro()' se seu construtor exigir todos os campos.
                        )
                        listaOriginal.add(livro)
                    }

                    // Reaplicar o filtro atual (ou o padrão) após carregar os novos dados
                    aplicarFiltro("alugados")

                    // Atualiza o texto de progresso
                    txtProgresso.text = "${listaOriginal.size}/12"
                }
            }
    }

    // ------------------------------------------------
    // 🧠 FUNÇÃO CENTRAL DE FILTRAGEM/ORDENAÇÃO
    // ------------------------------------------------
    private fun aplicarFiltro(tipo: String) {
        listaFiltrada.clear()

        when (tipo) {
            "alugados" -> {


                listaFiltrada.addAll(listaOriginal.sortedByDescending {
                    // Cuidado: anoLancamento não está sendo puxado no Livro atual,
                    // ajuste o Livro() ou use outro campo para ordenação se for o caso
                    it.anoLancamento.toIntOrNull() ?: 0
                })
            }
            "lidos" -> {

                listaFiltrada.addAll(listaOriginal.sortedBy {
                    it.anoLancamento.toIntOrNull() ?: 0
                })
            }
            "az" -> {

                listaFiltrada.addAll(listaOriginal.sortedBy { it.titulo.lowercase() })
            }
        }

        adapter.notifyDataSetChanged()
    }

    // 5. Desliga o listener quando a Activity é destruída para evitar vazamento de memória
    override fun onDestroy() {
        super.onDestroy()
        firestoreListener?.remove()
    }
}
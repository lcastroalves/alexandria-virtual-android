package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroAdapter
import com.example.alexandriavirtual20.model.Livro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaEmprestLivrosUsu : AppCompatActivity() {

    private lateinit var adapter: LivroAdapter
    private lateinit var searchView: SearchView

    private val listaOriginal = mutableListOf<Livro>()
    private val listaFiltrada = mutableListOf<Livro>()

    // Spinners
    private lateinit var spinnerAutor: Spinner
    private lateinit var spinnerGenero: Spinner
    private lateinit var spinnerPopulares: Spinner
    private lateinit var spinnerAno: Spinner

    private lateinit var recyclerView: RecyclerView
    private lateinit var fireBase: FirebaseFirestore
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_emprest_livros_usu)

        val btnConfirmar: Button = findViewById(R.id.btnConfirmar)
        val btnBack: ImageButton = findViewById(R.id.btnback)

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerLivros)

        spinnerAutor = findViewById(R.id.spinnerAutor)
        spinnerGenero = findViewById(R.id.spinnerGenero)
        spinnerPopulares = findViewById(R.id.spinnerMaisPopulares)
        spinnerAno = findViewById(R.id.spinnerLancamento)

        fireBase = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

        carregarLivros()
        configurarPesquisa()

        btnConfirmar.setOnClickListener {
            val selecionados = adapter.getSelecionados()

            if (selecionados.isEmpty()) {
                Toast.makeText(this, "Nenhum livro selecionado!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, TelaRetirarLivroUsu::class.java)
            intent.putParcelableArrayListExtra("livrosSelecionados", ArrayList(selecionados))
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // SISTEMA COMPLETO DE FILTRO DA LISTA
    private fun aplicarFiltros() {

        if (!::adapter.isInitialized) return

        val filtroAutor = spinnerAutor.selectedItem.toString()
        val filtroGenero = spinnerGenero.selectedItem.toString()
        val filtroAno = spinnerAno.selectedItem.toString()
        val filtroPopular = spinnerPopulares.selectedItem.toString()

        val pesquisa = searchView.query.toString().lowercase()

        listaFiltrada.clear()

        listaFiltrada.addAll(
            listaOriginal.filter { livro ->

                val autorOk = (filtroAutor == "Autor" || livro.autor == filtroAutor)
                val generoOk = (filtroGenero == "Gênero" || livro.genero == filtroGenero)
                val anoOk = (filtroAno == "Ano" || livro.anoLancamento == filtroAno)

                val pesquisaOk =
                    livro.titulo.lowercase().contains(pesquisa) ||
                            livro.autor.lowercase().contains(pesquisa)

                autorOk && generoOk && anoOk && pesquisaOk
            }
        )

        if (filtroPopular == "Mais Populares") {
            listaFiltrada.sortByDescending { it.avaliacoes }
        }

        adapter.notifyDataSetChanged()
    }

    private fun registrarFiltros() {

        val listenerFiltro = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                pos: Int,
                id: Long
            ) {
                aplicarFiltros()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerAutor.onItemSelectedListener = listenerFiltro
        spinnerGenero.onItemSelectedListener = listenerFiltro
        spinnerPopulares.onItemSelectedListener = listenerFiltro
        spinnerAno.onItemSelectedListener = listenerFiltro
    }

    private fun configurarPesquisa() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                aplicarFiltros()
                return true
            }
        })
    }

    // =============================================
    //     CARREGAR LIVROS + FILTRAR EMPRÉSTIMOS
    // =============================================
    private fun carregarLivros() {

        val userId = fbAuth.currentUser?.uid ?: return

        val emprestimosRef = fireBase.collection("emprestimo")
            .whereEqualTo("idUsuario", userId)
            .whereIn("situacao", listOf("pendente", "aprovado"))

        emprestimosRef.get()
            .addOnSuccessListener { emprestimos ->

                // IDs de livros que NÃO PODEM aparecer
                val livrosBloqueadosIds = emprestimos.documents.map {
                    it.getString("idLivro")
                }

                // AGORA BUSCA OS LIVROS E REMOVE OS BLOQUEADOS
                fireBase.collection("livros").get()
                    .addOnSuccessListener { query ->

                        listaOriginal.clear()

                        for (doc in query.documents) {

                            val id = doc.id

                            // SE O LIVRO ESTIVER BLOQUEADO → NÃO ENTRA NA LISTA
                            if (livrosBloqueadosIds.contains(id)) continue

                            val titulo = doc.getString("titulo") ?: ""
                            val autor = doc.getString("autor") ?: ""
                            val genero = doc.getString("genero") ?: ""
                            val ano = doc.getString("anoLancamento") ?: ""
                            val imagemBase64 = doc.getString("capa") ?: ""

                            val mediaAvaliacao = doc.getDouble("mediaAvaliacao") ?: 0.0
                            val totalAvaliacoes = doc.getLong("totalAvaliacoes") ?: 0L

                            listaOriginal.add(
                                Livro(
                                    id = id,
                                    titulo = titulo,
                                    autor = autor,
                                    genero = genero,
                                    anoLancamento = ano,
                                    capa = imagemBase64,
                                    avaliacoes = totalAvaliacoes.toInt(),
                                    mediaAvaliacao = mediaAvaliacao,
                                    totalAvaliacoes = totalAvaliacoes
                                )
                            )
                        }

                        listaFiltrada.clear()
                        listaFiltrada.addAll(listaOriginal)

                        recyclerView.layoutManager = LinearLayoutManager(this)

                        adapter = LivroAdapter(listaFiltrada) { livro ->
                            val intent = Intent(this, TelaAvaliacoesUsu::class.java)
                            intent.putExtra("livro", livro)
                            startActivity(intent)
                            finish()
                        }
                        recyclerView.adapter = adapter

                        carregarSpinners()
                        registrarFiltros()
                        aplicarFiltros()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar livros.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun carregarSpinners() {

        carregarSpinner(
            spinnerAutor,
            listaOriginal.map { it.autor }.distinct(),
            "Autor"
        )

        carregarSpinner(
            spinnerGenero,
            listaOriginal.map { it.genero }.distinct(),
            "Gênero"
        )

        carregarSpinner(
            spinnerAno,
            listaOriginal.map { it.anoLancamento }.distinct(),
            "Ano"
        )

        carregarSpinner(
            spinnerPopulares,
            listOf("Normal", "Mais Populares"),
            null
        )
    }

    private fun carregarSpinner(spinner: Spinner, opcoes: List<String>, extra: String?) {
        val lista = if (extra != null) listOf(extra) + opcoes else opcoes
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador
    }
}

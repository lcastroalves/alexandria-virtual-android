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

    private lateinit var spinnerAutor: Spinner
    private lateinit var spinnerGenero: Spinner
    private lateinit var spinnerPopulares: Spinner
    private lateinit var spinnerAno: Spinner

    private lateinit var recyclerView: RecyclerView
    private lateinit var fireBase: FirebaseFirestore
    private lateinit var fbAuth: FirebaseAuth

    private var livrosBloqueadosIds = listOf<String>()

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

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = LivroAdapter(listaFiltrada) { livro ->
            val intent = Intent(this, TelaAvaliacoesUsu::class.java)
            intent.putExtra("livro", livro)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        configurarPesquisa()

        observarEmprestimos()

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

    private fun observarEmprestimos() {
        val userId = fbAuth.currentUser?.uid ?: return

        fireBase.collection("emprestimo")
            .whereEqualTo("idUsuario", userId)
            .whereIn("situacao", listOf("pendente", "aprovado"))
            .addSnapshotListener { snapshots, error ->

                if (error != null) return@addSnapshotListener

                livrosBloqueadosIds = snapshots?.documents?.map {
                    it.getString("idLivro") ?: ""
                } ?: emptyList()

                observarLivros()
            }
    }

    private fun observarLivros() {
        fireBase.collection("livros")
            .addSnapshotListener { query, error ->

                if (error != null) return@addSnapshotListener

                listaOriginal.clear()

                for (doc in query!!) {

                    val id = doc.id

                    // 🔥 Bloqueia instantaneamente
                    if (livrosBloqueadosIds.contains(id)) continue

                    val livro = Livro(
                        id = id,
                        titulo = doc.getString("titulo") ?: "",
                        autor = doc.getString("autor") ?: "",
                        genero = doc.getString("genero") ?: "",
                        anoLancamento = doc.getString("anoLancamento") ?: "",
                        capa = doc.getString("capa") ?: "",
                        avaliacoes = (doc.getLong("totalAvaliacoes") ?: 0L).toInt(),
                        mediaAvaliacao = doc.getDouble("mediaAvaliacao") ?: 0.0,
                        totalAvaliacoes = doc.getLong("totalAvaliacoes") ?: 0L
                    )

                    listaOriginal.add(livro)
                }

                carregarSpinners()
                registrarFiltros()
                aplicarFiltros()
            }
    }

    private fun aplicarFiltros() {

        val pesquisa = searchView.query.toString().lowercase()

        val filtroAutor = spinnerAutor.selectedItem.toString()
        val filtroGenero = spinnerGenero.selectedItem.toString()
        val filtroAno = spinnerAno.selectedItem.toString()
        val filtroPopular = spinnerPopulares.selectedItem.toString()

        listaFiltrada.clear()

        listaFiltrada.addAll(
            listaOriginal.filter { livro ->

                val autorOk = filtroAutor == "Autor" || livro.autor == filtroAutor
                val generoOk = filtroGenero == "Gênero" || livro.genero == filtroGenero
                val anoOk = filtroAno == "Ano" || livro.anoLancamento == filtroAno

                val pesquisaOk =
                    livro.titulo.lowercase().contains(pesquisa) ||
                            livro.autor.lowercase().contains(pesquisa)

                autorOk && generoOk && anoOk && pesquisaOk
            }
        )

        if (filtroPopular == "Mais Populares") {
            listaFiltrada.sortByDescending { it.totalAvaliacoes }
        }

        adapter.notifyDataSetChanged()
    }


    private fun registrarFiltros() {
        val listener = object : AdapterView.OnItemSelectedListener {
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

        spinnerAutor.onItemSelectedListener = listener
        spinnerGenero.onItemSelectedListener = listener
        spinnerPopulares.onItemSelectedListener = listener
        spinnerAno.onItemSelectedListener = listener
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

    private fun carregarSpinners() {
        carregarSpinner(spinnerAutor, listaOriginal.map { it.autor }.distinct(), "Autor")
        carregarSpinner(spinnerGenero, listaOriginal.map { it.genero }.distinct(), "Gênero")
        carregarSpinner(spinnerAno, listaOriginal.map { it.anoLancamento }.distinct(), "Ano")
        carregarSpinner(spinnerPopulares, listOf("Normal", "Mais Populares"), null)
    }

    private fun carregarSpinner(spinner: Spinner, opcoes: List<String>, extra: String?) {
        val lista = if (extra != null) listOf(extra) + opcoes else opcoes
        val adaptador =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, lista)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador
    }
}

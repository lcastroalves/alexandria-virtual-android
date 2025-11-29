package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.SoliPend
import com.example.alexandriavirtual20.adapter.SoliPendAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaSolicitaPendUsu : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SoliPendAdapter

    private val listaEmprestimos = mutableListOf<SoliPend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_solicita_pend_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        searchView = findViewById(R.id.searchViewEventos)
        recyclerView = findViewById(R.id.recySoliPend)

        adapter = SoliPendAdapter(listaEmprestimos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        carregarEmprestimos()
        configurarPesquisa()
    }

    private fun carregarEmprestimos() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("emprestimo")                      // certifique-se do nome aqui
            .whereEqualTo("idUsuario", uid)
            .get()
            .addOnSuccessListener { documentos ->
                listaEmprestimos.clear()

                // filtra apenas os documentos com situacao pendente ou negado
                val emprestimosValidos = documentos.documents.filter { doc ->
                    val situacao = doc.getString("situacao") ?: ""
                    situacao == "pendente" || situacao == "negado"
                }

                if (emprestimosValidos.isEmpty()) {
                    adapter.updateList(listaEmprestimos)
                    return@addOnSuccessListener
                }

                // Para cada empréstimo válido, pega o idLivro (String) e busca o documento do livro
                for (doc in emprestimosValidos) {
                    val situacao = doc.getString("situacao") ?: ""
                    val idLivro = doc.getString("idLivro") ?: continue

                    // busca o livro pela id
                    db.collection("livros").document(idLivro).get()
                        .addOnSuccessListener { livroDoc ->
                            if (!livroDoc.exists()) return@addOnSuccessListener

                            val titulo = livroDoc.getString("titulo") ?: "Sem título"
                            val autor = livroDoc.getString("autor") ?: "Autor desconhecido"
                            val imagem = livroDoc.getString("capa") ?: ""

                            val emprestimo = SoliPend(
                                titulo = titulo,
                                autor = autor,
                                data = "Hoje",                // ajusta conforme sua lógica de datas
                                prazo = "3 dias",             // ou pegue do doc do empréstimo
                                local = "Biblioteca Central", // idem
                                imagem = imagem,
                                pendente = situacao == "pendente"
                            )

                            listaEmprestimos.add(emprestimo)

                            // atualiza adapter à medida que chegam os livros
                            adapter.updateList(listaEmprestimos)
                        }
                        .addOnFailureListener {
                            // opcional: log/Toast se falhar ao buscar o livro
                        }
                }
            }
            .addOnFailureListener {
                // opcional: tratar erro
            }
    }


    private fun configurarPesquisa() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(text: String?): Boolean {
                adapter.filtrar(text ?: "")
                return true
            }
        })
    }
}

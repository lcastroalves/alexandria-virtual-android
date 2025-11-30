package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.SoliPend
import com.example.alexandriavirtual20.adapter.SoliPendAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaSolicitaPendUsu : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SoliPendAdapter

    private val listaEmprestimos = mutableListOf<SoliPend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_solicita_pend_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recySoliPend)

        adapter = SoliPendAdapter(listaEmprestimos) { item ->
            confirmarDelecao(item)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        carregarEmprestimos()
    }

    private fun carregarEmprestimos() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("emprestimo")
            .whereEqualTo("idUsuario", uid)
            .get()
            .addOnSuccessListener { documentos ->
                listaEmprestimos.clear()

                val emprestimosValidos = documentos.documents.filter { doc ->
                    val situacao = doc.getString("situacao") ?: ""
                    situacao == "pendente" || situacao == "negado"
                }

                if (emprestimosValidos.isEmpty()) {
                    adapter.updateList(listaEmprestimos)
                    return@addOnSuccessListener
                }

                for (doc in emprestimosValidos) {

                    val situacao = doc.getString("situacao") ?: ""
                    val idLivro = doc.getString("idLivro") ?: continue
                    val idEmprestimo = doc.id

                    db.collection("livros").document(idLivro).get()
                        .addOnSuccessListener { livroDoc ->

                            if (!livroDoc.exists()) return@addOnSuccessListener

                            val titulo = livroDoc.getString("titulo") ?: "Sem título"
                            val autor = livroDoc.getString("autor") ?: "Autor desconhecido"
                            val imagem = livroDoc.getString("capa") ?: ""

                            // 🔥 AVALIAÇÃO DO LIVRO – EXATAMENTE COMO NO LIVROADAPTER
                            val avaliacaoTotal = livroDoc.getDouble("avaliacao") ?: 0.0
                            val qtdAvaliacoes = livroDoc.getLong("qtdAvaliacoes") ?: 0

                            val mediaAvaliacao =
                                if (qtdAvaliacoes > 0) avaliacaoTotal / qtdAvaliacoes else 0.0

                            listaEmprestimos.add(
                                SoliPend(
                                    idEmprestimo = idEmprestimo,
                                    titulo = titulo,
                                    autor = autor,
                                    data = doc.getString("dataSolicitacao") ?: "Data não informada",
                                    prazo = "3 dias",
                                    local = "Biblioteca Central",
                                    imagem = imagem,
                                    pendente = situacao == "pendente",
                                    avaliacao = mediaAvaliacao  // ⭐ agora vem com média
                                )
                            )

                            adapter.updateList(listaEmprestimos)
                        }
                }
            }
    }

    private fun confirmarDelecao(item: SoliPend) {
        AlertDialog.Builder(this)
            .setTitle("Excluir solicitação")
            .setMessage("Deseja realmente excluir a solicitação de \"${item.titulo}\"?")
            .setPositiveButton("Sim") { _, _ ->
                deletarEmprestimo(item)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deletarEmprestimo(item: SoliPend) {
        val db = FirebaseFirestore.getInstance()

        db.collection("emprestimo")
            .document(item.idEmprestimo)
            .delete()
            .addOnSuccessListener {
                adapter.removerSoliPend(item)
                Toast.makeText(this, "Solicitação removida.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao remover.", Toast.LENGTH_SHORT).show()
            }
    }
}

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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

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

                    /** 🔥 Correção aqui: dataSolicitacao é Timestamp */
                    val timestamp = doc.getTimestamp("dataSolicitacao")
                    val dataFormatada = if (timestamp != null) {
                        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        sdf.format(timestamp.toDate())
                    } else {
                        "Data não informada"
                    }

                    db.collection("livros")
                        .document(idLivro)
                        .get()
                        .addOnSuccessListener { livroDoc ->

                            if (!livroDoc.exists()) return@addOnSuccessListener

                            val titulo = livroDoc.getString("titulo") ?: "Sem título"
                            val subtitulo = livroDoc.getString("subtitulo") ?: ""
                            val autor = livroDoc.getString("autor") ?: "Autor desconhecido"
                            val imagem = livroDoc.getString("capa") ?: ""

                            val mediaAvaliacao = livroDoc.getDouble("mediaAvaliacao") ?: 0.0
                            val totalAvaliacoes = livroDoc.getLong("totalAvaliacoes") ?: 0

                            // arredondar para 1 casa
                            val mediaArredondada =
                                String.format("%.1f", mediaAvaliacao).replace(",", ".").toDouble()

                            listaEmprestimos.add(
                                SoliPend(
                                    idEmprestimo = idEmprestimo,
                                    titulo = titulo,
                                    subtitulo = subtitulo,
                                    autor = autor,
                                    data = dataFormatada,  // ✅ AGORA CORRETO
                                    prazo = "3 dias",
                                    local = "Biblioteca Central",
                                    imagem = imagem,
                                    pendente = situacao == "pendente",
                                    avaliacao = mediaArredondada,
                                    qtdAvaliacoes = totalAvaliacoes.toInt()
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

package com.example.alexandriavirtual20

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.Solicitacao
import com.example.alexandriavirtual20.adapter.SolicitacaoAdapter
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AdmTelaSolicPend : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var btnPendentes: TextView
    private lateinit var btnUsuarios: TextView

    private val listaSolicitacoes = mutableListOf<Solicitacao>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.adm_tela_solic_pend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerSolicitacoes)
        btnPendentes = view.findViewById(R.id.tabPendentes)
        btnUsuarios = view.findViewById(R.id.tabUsuarios)

        btnPendentes.setOnClickListener {
            (activity as? AdmAMain)?.replaceFragment(AdmTelaSolicPend())
        }

        btnUsuarios.setOnClickListener {
            (activity as? AdmAMain)?.replaceFragment(AdmTelaHistoricoEmpres())
        }

        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        PagerSnapHelper().attachToRecyclerView(recycler)

        carregarSolicitacoesPendentes()
    }

    private fun carregarSolicitacoesPendentes() {
        db.collection("emprestimo")
            .whereEqualTo("situacao", "pendente")
            .get()
            .addOnSuccessListener { docs ->
                listaSolicitacoes.clear()

                if (docs.isEmpty) {
                    recycler.adapter = SolicitacaoAdapter(emptyList(), {}, {})
                    return@addOnSuccessListener
                }

                for (doc in docs) {
                    val idLivro = doc.getString("idLivro") ?: continue
                    val idUsuario = doc.getString("idUsuario") ?: continue

                    // buscar livro + usuário
                    carregarLivroEUsuario(doc.id, idLivro, idUsuario)
                }
            }
    }

    private fun carregarLivroEUsuario(
        idEmprestimo: String,
        idLivro: String,
        idUsuario: String
    ) {
        val refLivros = db.collection("livros").document(idLivro)
        val refUsers = db.collection("usuarios").document(idUsuario)

        refLivros.get().addOnSuccessListener { livroDoc ->
            val titulo = livroDoc.getString("titulo") ?: "Sem título"
            val autor = livroDoc.getString("autor") ?: "Autor desconhecido"
            val imagem = livroDoc.getString("capa") ?: ""

            refUsers.get().addOnSuccessListener { userDoc ->
                val nomeUser = userDoc.getString("nome") ?: "Usuário"
                val email = userDoc.getString("email") ?: "Email não encontrado"

                val solicitacao = Solicitacao(
                    idEmprestimo = idEmprestimo,
                    titulo = titulo,
                    autor = autor,
                    usuario = nomeUser,
                    email = email,
                    data = "Hoje",
                    prazo = "3 dias",
                    local = "Biblioteca Central",
                    capa = imagem,
                )

                listaSolicitacoes.add(solicitacao)

                recycler.adapter = SolicitacaoAdapter(
                    listaSolicitacoes,
                    onAutorizar = { position ->
                        val item = listaSolicitacoes[position]
                        atualizarSituacaoEmprestimo(idEmprestimo, "aprovado")
                        removerItemDaLista(position)
                    },
                    onRecusar = { position ->
                        val item = listaSolicitacoes[position]
                        atualizarSituacaoEmprestimo(idEmprestimo, "negado")
                        removerItemDaLista(position)
                    }
                )

            }
        }
    }

    private fun atualizarSituacaoEmprestimo(id: String, novaSituacao: String) {

        val ref = db.collection("emprestimo").document(id)

        // Se o admin aprovar → adicionar prazo de 30 dias
        if (novaSituacao == "aprovado") {

            val agora = com.google.firebase.Timestamp.now()

            val dataPrazo = java.util.Calendar.getInstance().apply {
                time = agora.toDate()
                add(java.util.Calendar.DAY_OF_YEAR, 30)
            }.time

            ref.update(
                mapOf(
                    "situacao" to novaSituacao,
                    "prazo" to com.google.firebase.Timestamp(dataPrazo)
                )
            ).addOnSuccessListener {
                carregarSolicitacoesPendentes()
            }.addOnFailureListener {
                it.printStackTrace()
            }

        } else {
            // Se negado → apenas atualiza situação
            ref.update("situacao", novaSituacao)
                .addOnSuccessListener {
                    carregarSolicitacoesPendentes()
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }

    private fun removerItemDaLista(position: Int) {
        listaSolicitacoes.removeAt(position)
        recycler.adapter?.notifyItemRemoved(position)
    }
    fun autorizarRetirada(idEmprestimo: String) {
        val firestore = FirebaseFirestore.getInstance()

        val atualizacoes = hashMapOf<String, Any>(
            "situacao" to "aprovado",
            "data" to FieldValue.serverTimestamp(), // 🌟 CRIA O CAMPO 'data' COM O TIMESTAMP ATUAL DO SERVIDOR
            "statusDevolucao" to "Em dia" // Opcional: define um status inicial para o Histórico
        )

        firestore.collection("emprestimo").document(idEmprestimo)
            .update(atualizacoes)
            .addOnSuccessListener {
                // Sucesso na aprovação e criação do campo 'data'
            }
            .addOnFailureListener { e ->
                // Tratar erro
            }
    }


}

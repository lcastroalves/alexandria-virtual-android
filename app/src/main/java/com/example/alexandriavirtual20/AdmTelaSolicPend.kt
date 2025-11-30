package com.example.alexandriavirtual20

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.Solicitacao
import com.example.alexandriavirtual20.adapter.SolicitacaoAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class AdmTelaSolicPend : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SolicitacaoAdapter
    private val listaSolicitacoes = mutableListOf<Solicitacao>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.adm_tela_solic_pend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerSolicitacoes)
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        PagerSnapHelper().attachToRecyclerView(recycler)

        // ★ Navegação corrigida
        view.findViewById<TextView>(R.id.tabPendentes).setOnClickListener { }
        view.findViewById<TextView>(R.id.tabUsuarios).setOnClickListener {
            (activity as? AdmAMain)?.replaceFragment(AdmTelaHistoricoEmpres())
        }

        adapter = SolicitacaoAdapter(
            listaSolicitacoes,
            onAutorizar = { item ->
                atualizarSituacaoEmprestimo(item, true)
                adapter.removerItem(item)
            },
            onRecusar = { item ->
                atualizarSituacaoEmprestimo(item, false)
                adapter.removerItem(item)
            }
        )

        recycler.adapter = adapter

        carregarSolicitacoesPendentes()
    }

    private fun carregarSolicitacoesPendentes() {
        db.collection("emprestimo")
            .whereEqualTo("situacao", "pendente")
            .get()
            .addOnSuccessListener { docs ->
                listaSolicitacoes.clear()
                adapter.notifyDataSetChanged()

                for (doc in docs) {

                    val idLivro = doc.getString("idLivro") ?: continue
                    val idUsuario = doc.getString("idUsuario") ?: continue

                    val dataSolicitacao =
                        doc.getTimestamp("dataSolicitacao")?.toDate()?.time ?: 0L

                    carregarLivroEUsuario(
                        idEmprestimo = doc.id,
                        idLivro = idLivro,
                        idUsuario = idUsuario,
                        dataSolicitacao = dataSolicitacao
                    )
                }
            }
    }

    private fun carregarLivroEUsuario(
        idEmprestimo: String,
        idLivro: String,
        idUsuario: String,
        dataSolicitacao: Long
    ) {
        val refLivros = db.collection("livros").document(idLivro)
        val refUsers = db.collection("usuario").document(idUsuario)

        refLivros.get().addOnSuccessListener { livro ->
            val titulo = livro.getString("titulo") ?: "Sem título"
            val autor = livro.getString("autor") ?: "Autor desconhecido"
            val capa = livro.getString("capa") ?: ""

            refUsers.get().addOnSuccessListener { userDoc ->

                val usuarioNome = userDoc.getString("nome") ?: "Usuário"
                val email = userDoc.getString("email") ?: "E-mail"

                val solicitacao = Solicitacao(
                    idEmprestimo = idEmprestimo,
                    idLivro = idLivro,
                    idUsuario = idUsuario,
                    titulo = titulo,
                    autor = autor,
                    usuario = usuarioNome,
                    email = email,
                    dataSolicitacao = dataSolicitacao,
                    prazo = "3 dias",
                    local = "Biblioteca Central",
                    capa = capa
                )

                listaSolicitacoes.add(solicitacao)
                adapter.notifyItemInserted(listaSolicitacoes.size - 1)
            }
        }
    }

    // ★★★★★ AQUI SAI A NOTIFICAÇÃO QUANDO O ADMIN APROVA ★★★★★
    private fun atualizarSituacaoEmprestimo(
        item: Solicitacao,
        aprovado: Boolean
    ) {
        val ref = db.collection("emprestimo").document(item.idEmprestimo)

        if (aprovado) {
            val agora = Timestamp.now()

            val dataPrazo = java.util.Calendar.getInstance().apply {
                time = agora.toDate()
                add(java.util.Calendar.DAY_OF_YEAR, 30)
            }.time

            ref.update(
                mapOf(
                    "situacao" to "aprovado",
                    "prazo" to Timestamp(dataPrazo)
                )
            )

            // ★ CRIA NOTIFICAÇÃO PARA O USUÁRIO
            criarNotificacaoAprovado(item)

        } else {
            ref.update("situacao", "negado")
        }
    }

    // ★★★ Função que cria a notificação (como a do evento) ★★★
    private fun criarNotificacaoAprovado(item: Solicitacao) {

        val notificacao = hashMapOf(
            "tipo" to "emprestimo",
            "nome" to item.titulo,
            "dataSolicitacao" to item.dataSolicitacao,
            "prazo" to System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
            "imagem" to item.capa
        )

        db.collection("usuario")
            .document(item.idUsuario)
            .collection("notificacoes")
            .add(notificacao)
    }
}

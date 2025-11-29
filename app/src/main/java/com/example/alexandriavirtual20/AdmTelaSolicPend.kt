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
                    onAutorizar = {
                        atualizarSituacaoEmprestimo(idEmprestimo, "aprovado")
                    },
                    onRecusar = {
                        atualizarSituacaoEmprestimo(idEmprestimo, "negado")
                    }
                )
            }
        }
    }

    private fun atualizarSituacaoEmprestimo(id: String, novaSituacao: String) {
        db.collection("emprestimo").document(id)
            .update("situacao", novaSituacao)
            .addOnSuccessListener {
                carregarSolicitacoesPendentes()
            }
    }

}

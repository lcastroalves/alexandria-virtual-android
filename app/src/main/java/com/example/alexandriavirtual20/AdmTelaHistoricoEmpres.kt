package com.example.alexandriavirtual20

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.model.HistoricoEmprestimo
import com.example.alexandriavirtual20.adapter.HistoricoEmprestimoAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Locale

// Definições de Argumentos (mantidas do seu template original)
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdmTelaHistoricoEmpres : Fragment() {

    private val TAG = "AdmHistorico"
    private val db = FirebaseFirestore.getInstance()
    private var firebaseListener: ListenerRegistration? = null

    // Lista final que alimenta o Adapter
    private val listaHistorico = mutableListOf<HistoricoEmprestimo>()
    private lateinit var adapter: HistoricoEmprestimoAdapter

    private var documentosPendentes = 0
    private var documentosProcessados = 0
    // Formato de data para exibir (Ex: "09 de Set")
    // Este formato será usado tanto para a data de empréstimo (dataSolicitacao) quanto para o prazo
    private val dateFormat = SimpleDateFormat("dd 'de' MMM", Locale("pt", "BR"))

    // ... (onCreate e métodos de argumento padrão omitidos por brevidade)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.adm_tela_historico_empres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerLivrosRetirados)
        val tabPendentes = view.findViewById<TextView>(R.id.tabPendentes)
        val tabUsuarios = view.findViewById<TextView>(R.id.tabUsuarios)

        // Configuração das abas para navegação
        tabPendentes.setOnClickListener {
            (activity as? AdmAMain)?.replaceFragment(AdmTelaSolicPend())
        }
        tabUsuarios.setOnClickListener {
            // Se esta é a tela de Histórico, não precisa navegar para si mesma
        }

        // 🌟 Inicialização do Adapter com as ações de Histórico (Devolução/Atraso)
        adapter = HistoricoEmprestimoAdapter(
            listaHistorico,
            onDevolver = { item ->
                Toast.makeText(requireContext(), "Confirmando devolução de ${item.titulo}", Toast.LENGTH_SHORT).show()
                // Implementar updateStatusEmprestimo(item.idDocumento, "Devolvido")
            },
            onAtrasar = { item ->
                Toast.makeText(requireContext(), "Registrando Atraso para ${item.titulo}", Toast.LENGTH_SHORT).show()
                // Implementar updateStatusEmprestimo(item.idDocumento, "Atrasado")
            })

        recycler.adapter = adapter
        // Configuração do RecyclerView para deslizar de tela cheia (PagerSnapHelper)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(recycler)

        // Carrega os dados do Firebase
        carregarHistoricoEmprestimos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        firebaseListener?.remove()
    }

    private fun carregarHistoricoEmprestimos() {
        firebaseListener?.remove()

        // 1. Busca todos os empréstimos onde 'situacao' é "aprovado"
        firebaseListener = db.collection("emprestimo")
            .whereEqualTo("situacao", "aprovado")
            .addSnapshotListener { snapshots, error ->

                if (error != null || snapshots == null) {
                    Log.e(TAG, "Erro ao carregar histórico: ${error?.message}")
                    return@addSnapshotListener
                }

                documentosProcessados = 0
                documentosPendentes = snapshots.size()
                listaHistorico.clear()

                if (snapshots.isEmpty) {
                    adapter.notifyDataSetChanged()
                    return@addSnapshotListener
                }

                snapshots.forEach { doc ->
                    val idLivro = doc.getString("idLivro")
                    val idUsuario = doc.getString("idUsuario")

                    if (idLivro != null && idUsuario != null) {
                        // Inicia o processo de busca tripla
                        carregarDetalhes(doc.id, idLivro, idUsuario, doc)
                    } else {
                        processarFinalizar()
                    }
                }
            }

    }


    // 2 & 3. Função de busca tripla (Livro e Usuário)
    private fun carregarDetalhes(
        idEmprestimo: String,
        idLivro: String,
        idUsuario: String,
        emprestimoDoc: QueryDocumentSnapshot
    ) {
        val refLivros = db.collection("livros").document(idLivro)
        val refUsers = db.collection("usuario").document(idUsuario)

        refLivros.get().addOnSuccessListener { livroDoc ->
            val titulo = livroDoc.getString("titulo") ?: "Sem título"
            val autor = livroDoc.getString("autor") ?: "Autor desconhecido"
            val capa = livroDoc.getString("capa") ?: ""

            refUsers.get().addOnSuccessListener { userDoc ->
                val nomeUser = userDoc.getString("nome") ?: "Usuário Desconhecido"
                val email = userDoc.getString("email") ?: "Email não encontrado"

                // Extrai e formata os dados do Empréstimo
                val dataPrevistaDevolucao = emprestimoDoc.getTimestamp("prazo")?.toDate()

                // 🌟 LEITURA CORRIGIDA: Usa o campo 'dataSolicitacao'
                val dataSolicitacaoTimestamp = emprestimoDoc.getTimestamp("dataSolicitacao")
                val statusDevolucao = emprestimoDoc.getString("statusDevolucao") ?: "Em dia"

                // Formatação das datas
                // Se a data de solicitação existir, formata; senão, usa 'S/ Data'
                val dataEmpStr = dataSolicitacaoTimestamp?.let { dateFormat.format(it.toDate()) } ?: "S/ Data"
                val prazoStr = dataPrevistaDevolucao?.let { dateFormat.format(it) } ?: "S/ Prazo"

                val historicoItem = HistoricoEmprestimo(
                    idDocumento = idEmprestimo,
                    idLivro = idLivro,
                    idUsuario = idUsuario,
                    dataEmprestimo = dataEmpStr,          // 🌟 Data real da Solicitação no formato "dd de MMM"
                    dataPrevistaDevolucao = prazoStr,
                    statusDevolucao = statusDevolucao,
                    titulo = titulo,
                    autor = autor,
                    capa = capa,
                    nomeUsuario = nomeUser,
                    emailUsuario = email
                )

                listaHistorico.add(historicoItem)
                processarFinalizar()

            }.addOnFailureListener {
                Log.w(TAG, "Falha ao buscar usuário $idUsuario", it)
                processarFinalizar()
            }
        }.addOnFailureListener {
            Log.w(TAG, "Falha ao buscar livro $idLivro", it)
            processarFinalizar()
        }
    }

    // Garante que o adapter só seja notificado após o processamento de todos os documentos
    private fun processarFinalizar() {
        documentosProcessados++
        if (documentosProcessados == documentosPendentes) {
            adapter.notifyDataSetChanged()
        }
    }
}
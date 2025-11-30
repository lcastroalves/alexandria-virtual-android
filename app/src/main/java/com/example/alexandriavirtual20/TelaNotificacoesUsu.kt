package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.NotificacaoAdapter
import com.example.alexandriavirtual20.model.Notificacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaNotificacoesUsu : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var fb: FirebaseFirestore
    private lateinit var adapter: NotificacaoAdapter
    private val listaNotificacoes = mutableListOf<Notificacao>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_notificacoes_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recy)
        fb = FirebaseFirestore.getInstance()

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapter = NotificacaoAdapter(
            listaNotificacoes,
            onExcluirClick = { notificacao ->
                adapter.removerNotificacao(notificacao)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        observarNotificacoes()
    }

    private fun observarNotificacoes() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            Toast.makeText(this, "Erro: usuário não logado.", Toast.LENGTH_SHORT).show()
            return
        }

        val userNotificacoesRef = fb.collection("usuario")
            .document(uid)
            .collection("notificacoes")

        // 🔥 Agora esta é a ÚNICA fonte de notificações
        userNotificacoesRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Toast.makeText(this, "Erro ao carregar notificações.", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            listaNotificacoes.clear()

            for (document in snapshots!!) {

                // --- Identificar formato do prazo (Long, Timestamp ou String) ---
                val prazoField = document.get("prazo")
                var millisPrazo = 0L

                when (prazoField) {
                    is com.google.firebase.Timestamp -> millisPrazo = prazoField.toDate().time
                    is Long -> millisPrazo = prazoField
                    is String -> {
                        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy")
                        millisPrazo = sdf.parse(prazoField)?.time ?: 0L
                    }
                }

                // --- Calcular diferença em dias ---
                val agora = System.currentTimeMillis()
                val diferencaDias =
                    ((millisPrazo - agora) / (1000L * 60 * 60 * 24)).toInt()

                // --- Classificação do prazo (1 = urgente, 2 = médio, 3 = tranquilo) ---
                val prazoInt = when {
                    diferencaDias <= 7 -> 1
                    diferencaDias <= 14 -> 2
                    else -> 3
                }

                val notificacao = Notificacao(
                    nome = document.getString("nome") ?: "",
                    data = java.text.SimpleDateFormat("dd/MM/yyyy").format(millisPrazo),
                    imagem = document.getString("imagem") ?: "",
                    tipo = document.getString("tipo") ?: "",
                    prazo = prazoInt,
                    dias = diferencaDias,
                    mensagem = document.getString("mensagem") ?: ""
                )

                listaNotificacoes.add(notificacao)
            }

            // 🔥 Ordena por dias restantes (mais urgente primeiro)
            listaNotificacoes.sortBy { it.dias }

            adapter.notifyDataSetChanged()
        }
    }
}

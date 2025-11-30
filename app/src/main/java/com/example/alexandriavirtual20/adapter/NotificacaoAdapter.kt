package com.example.alexandriavirtual20.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Notificacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Base64

class NotificacaoAdapter(
    private val notificacoes: MutableList<Notificacao>,
    private val onExcluirClick: ((Notificacao) -> Unit)? = null
) : RecyclerView.Adapter<NotificacaoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagem: ImageView = view.findViewById(R.id.imagemAddAdm)
        val nome: TextView = view.findViewById(R.id.nomeEventoLista)
        val tipo: TextView = view.findViewById(R.id.dataEventoLista)
        val data: TextView = view.findViewById(R.id.nomeAddAdm)
        val mensagem: TextView = view.findViewById(R.id.descricaoEventoLista)
        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.u_cardview_notificacao, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notificacao = notificacoes[position]

        // --- Decodificar imagem ---
        val bytes = Base64.decode(notificacao.imagem, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.imagem.setImageBitmap(bitmap)

        holder.nome.text = notificacao.nome

        // --- Corrige tipo visual ---
        holder.tipo.text = if (notificacao.tipo == "emprestimo") "Devolução"
        else "Evento"

        // --- Exibir data de prazo corretamente ---
        holder.data.text = notificacao.data

        val dias = notificacao.dias
        val semanas = dias / 7

        // --- Atrasado ---
        if (dias < 0) {
            val redColor = ContextCompat.getColor(holder.itemView.context, R.color.red)
            holder.mensagem.setTextColor(redColor)

            holder.mensagem.text = if (notificacao.tipo == "evento")
                "Evento finalizado!"
            else
                "Devolução atrasada!"

            return
        }

        // === Agora começam as mensagens baseadas no prazo ===
        val red = ContextCompat.getColor(holder.itemView.context, R.color.red)
        val yellow = ContextCompat.getColor(holder.itemView.context, R.color.darkYellow)
        val green = ContextCompat.getColor(holder.itemView.context, R.color.green)

        when (notificacao.prazo) {
            1 -> {
                holder.mensagem.setTextColor(red)
                holder.mensagem.text =
                    if (notificacao.tipo == "evento")
                        "O evento está próximo!"
                    else
                        "O tempo para devolução está acabando!"
            }

            2 -> {
                holder.mensagem.setTextColor(yellow)
                holder.mensagem.text =
                    if (notificacao.tipo == "evento")
                        "Faltam duas semanas para o evento!"
                    else
                        "Faltam duas semanas para o prazo de devolução!"
            }

            else -> {
                holder.mensagem.setTextColor(green)
                holder.mensagem.text =
                    if (notificacao.tipo == "evento")
                        "Faltam $semanas semanas para o evento!"
                    else
                        "Faltam $semanas semanas para devolver o livro!"
            }
        }

        // --- Botão excluir ---
        holder.btnExcluir.setOnClickListener {
            onExcluirClick?.invoke(notificacao)
        }
    }

    fun removerNotificacao(notificacao: Notificacao) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("usuario")
            .document(uid)
            .collection("notificacoes")
            .whereEqualTo("nome", notificacao.nome)
            .get()
            .addOnSuccessListener { query ->
                for (doc in query.documents) {
                    doc.reference.delete()
                }
            }
            .addOnSuccessListener {
                val pos = notificacoes.indexOf(notificacao)
                if (pos != -1) {
                    notificacoes.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Erro ao deletar notificação", it)
            }
    }

    override fun getItemCount(): Int = notificacoes.size
}

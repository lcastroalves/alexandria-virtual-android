package com.example.alexandriavirtual20.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Notificacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificacaoAdapter(
    private val context: Context, // ✅ Contexto para os Toasts
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

        // --- 1. Decodificar imagem ---
        if (notificacao.imagem.isNotEmpty()) {
            try {
                val bytes = Base64.decode(notificacao.imagem, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.imagem.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.imagem.setImageResource(R.drawable.livro) // Imagem padrão se falhar
            }
        }

        // --- 2. Preencher Textos Básicos ---
        holder.nome.text = notificacao.nome
        holder.data.text = notificacao.data

        // --- 3. Corrigir Tipo Visual ---
        holder.tipo.text = if (notificacao.tipo == "emprestimo") "Devolução" else "Evento"

        // --- 4. Lógica de Cores e Mensagens (A PARTE QUE TINHA SUMIDO) ---
        val dias = notificacao.dias
        val semanas = dias / 7

        // Se estiver atrasado (dias negativos)
        if (dias < 0) {
            val redColor = ContextCompat.getColor(context, R.color.red)
            holder.mensagem.setTextColor(redColor)
            holder.mensagem.text = if (notificacao.tipo == "evento")
                "Evento finalizado!"
            else
                "Devolução atrasada!"
        } else {
            // Se não estiver atrasado, verifica o prazo
            val red = ContextCompat.getColor(context, R.color.red)
            val yellow = ContextCompat.getColor(context, R.color.darkYellow)
            val green = ContextCompat.getColor(context, R.color.green)

            when (notificacao.prazo) {
                1 -> { // Urgente (<= 7 dias)
                    holder.mensagem.setTextColor(red)
                    holder.mensagem.text =
                        if (notificacao.tipo == "evento") "O evento está próximo!"
                        else "O tempo para devolução está acabando!"
                }
                2 -> { // Médio (<= 14 dias)
                    holder.mensagem.setTextColor(yellow)
                    holder.mensagem.text =
                        if (notificacao.tipo == "evento") "Faltam duas semanas para o evento!"
                        else "Faltam duas semanas para o prazo de devolução!"
                }
                else -> { // Tranquilo (> 14 dias)
                    holder.mensagem.setTextColor(green)
                    holder.mensagem.text =
                        if (notificacao.tipo == "evento") "Faltam $semanas semanas para o evento!"
                        else "Faltam $semanas semanas para devolver o livro!"
                }
            }
        }

        // --- 5. Clique do Botão Excluir ---
        holder.btnExcluir.setOnClickListener {
            onExcluirClick?.invoke(notificacao)
        }
    }

    fun removerNotificacao(notificacao: Notificacao) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Validação de ID
        if (notificacao.id.isEmpty()) {
            Log.e("Firestore", "ERRO: Tentativa de excluir notificação com ID vazio.")
            return
        }

        FirebaseFirestore.getInstance()
            .collection("usuario")
            .document(uid)
            .collection("notificacoes")
            .document(notificacao.id) // ✅ Deleta pelo ID
            .delete()
            .addOnSuccessListener {
                val pos = notificacoes.indexOf(notificacao)
                if (pos != -1) {
                    notificacoes.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erro ao deletar: ${e.message}")
            }
    }

    override fun getItemCount(): Int = notificacoes.size
}
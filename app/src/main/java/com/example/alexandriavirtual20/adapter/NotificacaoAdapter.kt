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


class NotificacaoAdapter (
    private val notificacoes: MutableList<Notificacao>,
    private val onExcluirClick: ((Notificacao) -> Unit)? = null
) :
    RecyclerView.Adapter<NotificacaoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagem: ImageView = view.findViewById(R.id.imagemAddAdm)
        val nome: TextView = view.findViewById(R.id.nomeEventoLista)
        val tipo: TextView = view.findViewById(R.id.dataEventoLista)
        val data: TextView = view.findViewById(R.id.nomeAddAdm)
        val mensagem: TextView = view.findViewById(R.id.descricaoEventoLista)
        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_notificacao, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notificacao = notificacoes[position]

        val bytes = Base64.decode(notificacao.imagem, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.imagem.setImageBitmap(bitmap)

        holder.nome.text = notificacao.nome
        holder.tipo.text = notificacao.tipo
        holder.data.text = notificacao.data
        holder.mensagem.text = notificacao.mensagem

        val dias = notificacao.dias
        val semanas = dias / 7

        if (notificacao.prazo <= 0) {

            if (notificacao.tipo == "evento") {
                holder.mensagem.text = "Evento finalizado!"

            } else {
                holder.mensagem.text = "Devolução atrasada!"
            }

            notificacoes.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)

            return
        }

        else if (notificacao.prazo <= 1 && notificacao.prazo > 0) {
            val redColor = ContextCompat.getColor(holder.itemView.context, R.color.red)
            holder.mensagem.setTextColor(redColor)

            if (notificacao.tipo == "evento") {
                holder.mensagem.text = "Faltam " + dias + " dias para realização do evento!"
            }

            else {
                holder.mensagem.text = "Faltam " + dias + " dias para fim do prazo!"
            }
        }

        else if (notificacao.prazo == 2) {
            val yellowColor = ContextCompat.getColor(holder.itemView.context, R.color.darkYellow)
            holder.mensagem.setTextColor(yellowColor)

            if (notificacao.tipo == "evento") {
                holder.mensagem.text = "Faltam duas semanas para realização do evento!"
            }

            else {
                holder.mensagem.text = "Faltam duas semanas para fim do prazo de devolução!"
            }
        }

        else {
            val greenColor = ContextCompat.getColor(holder.itemView.context, R.color.green)
            holder.mensagem.setTextColor(greenColor)

            if (notificacao.tipo == "evento") {
                holder.mensagem.text = "Faltam " + semanas + " semanas para realização do evento!"
            }

            else {
                holder.mensagem.text = "Faltam " + semanas + " semanas para fim do prazo de devolução!"
            }
        }

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
            .addOnFailureListener {
                Log.e("Firestore", "Erro ao deletar notificação", it)
            }
            .addOnSuccessListener {
                val position = notificacoes.indexOf(notificacao)
                if (position > -1) {
                    notificacoes.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                }
            }
            .addOnFailureListener {
            }
    }

    override fun getItemCount(): Int {
        return notificacoes.size
    }

    }
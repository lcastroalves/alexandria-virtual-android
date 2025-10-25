package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R

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

    override fun onBindViewHolder(holder: NotificacaoAdapter.ViewHolder, position: Int) {
        val notificacao = notificacoes[position]

        holder.imagem.setImageResource(notificacao.imagem)
        holder.nome.text = notificacao.nome
        holder.tipo.text = notificacao.tipo
        holder.data.text = notificacao.data
        holder.mensagem.text = notificacao.mensagem

        if (notificacao.prazo == 1) {
            val redColor = ContextCompat.getColor(holder.itemView.context, R.color.red)
            holder.mensagem.setTextColor(redColor)
        } else if (notificacao.prazo == 2) {
            val yellowColor = ContextCompat.getColor(holder.itemView.context, R.color.yellow)
            holder.mensagem.setTextColor(yellowColor)
        } else {
            val greenColor = ContextCompat.getColor(holder.itemView.context, R.color.green)
            holder.mensagem.setTextColor(greenColor)
        }

        holder.btnExcluir.setOnClickListener {
            onExcluirClick?.invoke(notificacao)
        }
    }

    fun removerNotificacao(notificacao: Notificacao) {
        val position = notificacoes.indexOf(notificacao)
        if (position > -1) {
            notificacoes.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun getItemCount(): Int {
        return notificacoes.size
    }

    }
package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.adapter.AtividadeAdapterTelaInicio.ViewHolder
import com.example.alexandriavirtual20.model.Mensagem

class MensagemAdapter (
    private val mensagens : MutableList<Mensagem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val TIPO_USER = 1
        private const val TIPO_BOT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (mensagens[position].isUsuario) TIPO_USER else TIPO_BOT
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val conteudoMensagem : TextView = view.findViewById(R.id.msgUsuario)
    }

    inner class BotViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val conteudoMensagem: TextView = view.findViewById(R.id.msgChatbot)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == TIPO_USER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.u_mensagem_usuario, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.u_mensagem_chatbot, parent, false)
            BotViewHolder(view)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val mensagem = mensagens[position]

        if(holder is UserViewHolder){
            holder.conteudoMensagem.text = mensagem.texto
        } else if (holder is BotViewHolder){
            holder.conteudoMensagem.text = mensagem.texto
        }
    }

    fun adicionarMensagem(msg: Mensagem) {
        mensagens.add(msg)
        notifyItemInserted(mensagens.size - 1)
    }

    override fun getItemCount(): Int {
        return mensagens.size
    }

}
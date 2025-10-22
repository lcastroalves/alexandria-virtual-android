package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.adapter.ProdutoAdapter.ViewHolder
import org.w3c.dom.Text

class NotificacaoAdapter (
    private val notificacoes: List<Notificacao>,
) :
    RecyclerView.Adapter<NotificacaoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagem: ImageView = view.findViewById(R.id.imagem)
        val nome: TextView = view.findViewById(R.id.nome)
        val tipo: TextView = view.findViewById(R.id.tipo)
        val data: TextView = view.findViewById(R.id.data)
        val mensagem: TextView = view.findViewById(R.id.mensagem)
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

        holder.btnExcluir.setOnClickListener {
            // colocar logica para excluir
        }
    }

    override fun getItemCount(): Int {
        return notificacoes.size
    }

    }
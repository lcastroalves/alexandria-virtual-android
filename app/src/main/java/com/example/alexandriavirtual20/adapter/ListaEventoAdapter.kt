package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Evento

class ListaEventoAdapter (
    private val eventos: MutableList<Evento>,
    private val onClick: (Evento) -> Unit
) :
    RecyclerView.Adapter<ListaEventoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagem: ImageView = view.findViewById(R.id.imagemEventoLista)
        val nome: TextView = view.findViewById(R.id.nome)
        val data: TextView = view.findViewById(R.id.data)
        val descricao: TextView = view.findViewById(R.id.descricaoBreve)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_evento_da_lista, parent, false)

        val viewHolder = ViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onClick(eventos[position])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ListaEventoAdapter.ViewHolder, position: Int) {
        val evento = eventos[position]

        holder.imagem.setImageResource(evento.imagem)
        holder.nome.text = evento.nome
        holder.data.text = evento.data
        holder.descricao.text = evento.descricao
    }

    override fun getItemCount(): Int {
        return eventos.size
    }
}
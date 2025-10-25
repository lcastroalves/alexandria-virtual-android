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
) : //Construtor do adapter
    RecyclerView.Adapter<ListaEventoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagem: ImageView = view.findViewById(R.id.imagemAddAdm)
        val nome: TextView = view.findViewById(R.id.nomeEventoLista)
        val data: TextView = view.findViewById(R.id.dataEventoLista)
        val descricao: TextView = view.findViewById(R.id.descricaoEventoLista)
    }
    //View Holder, recicla variáveis pra não gastar tanta memória com destruição e criação em listas
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_evento_da_lista, parent, false)
        return ViewHolder(view)
    }
    // Inflater "infla" o View Holder com elementos do cardview

    override fun onBindViewHolder(holder: ListaEventoAdapter.ViewHolder, position: Int) {
        val evento = eventos[position]

        holder.imagem.setImageResource(evento.imagem)
        holder.nome.text = evento.nome
        holder.data.text = evento.data
        holder.descricao.text = evento.descricao

        holder.imagem.setOnClickListener {
            onClick(evento)
        }
    }

    override fun getItemCount(): Int {
        return eventos.size
    }
}
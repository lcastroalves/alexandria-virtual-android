package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Atividade

class AtividadeAdapter (
    private val atividades: MutableList<Atividade>,
    private val onClick: (Atividade) -> Unit
) :
    RecyclerView.Adapter<AtividadeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagem: ImageView = view.findViewById(R.id.imagemAtividadeLista)
        val nome: TextView = view.findViewById(R.id.nomeAtividadeLista)
        val descricao: TextView = view.findViewById(R.id.descricaoAtividadeLista)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_atividade, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AtividadeAdapter.ViewHolder, position: Int) {
        val atividade = atividades[position]

        //holder.imagem.setImageResource(atividade.imagem)
        holder.nome.text = atividade.nome
        holder.descricao.text = atividade.descricao

        holder.imagem.setOnClickListener {
            onClick(atividade)
        }
    }

    override fun getItemCount(): Int {
        return atividades.size
    }
}
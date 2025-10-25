package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Atividade

class AtividadeAdapterTelaInicio(
    private val atividades : MutableList<Atividade>,
    private val onAtividadeClick : (Atividade) -> Unit
) : RecyclerView.Adapter<AtividadeAdapterTelaInicio.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraintLayout)
        val capaAtividade : ImageView = view.findViewById(R.id.capaAtividade)
        val nomeAtividade : TextView = view.findViewById(R.id.nomeAtividade)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_atividade_inicio, parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val atividade = atividades[position]

        holder.capaAtividade.setImageResource(atividade.imagem)
        holder.nomeAtividade.text = atividade.nome

        holder.constraintLayout.setOnClickListener {
            onAtividadeClick(atividade)
        }

    }

    override fun getItemCount(): Int {
        return atividades.size
    }


}
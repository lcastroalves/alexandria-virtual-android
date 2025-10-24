package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.SoliPend

class SoliPendAdapter (
    private val soliPends: MutableList<SoliPend>,
    private val onClick: (SoliPend) -> Unit
) :
    RecyclerView.Adapter<SoliPendAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagem: ImageView = view.findViewById(R.id.imagemSoliPend)
        val nome: TextView = view.findViewById(R.id.nomeSoliPend)
        val autor: TextView = view.findViewById(R.id.autorSoliPend)
        val pendente: ImageView = view.findViewById(R.id.pendenteSoliPend)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_solicitacoes_pend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoliPendAdapter.ViewHolder, position: Int) {
        val soliPend = soliPends[position]

        holder.imagem.setImageResource(soliPend.imagem)
        holder.nome.text = soliPend.nome
        holder.autor.text = soliPend.autor
        holder.pendente

    }

    fun onExcluirClick(position: Int) {

    }

    override fun getItemCount(): Int {
        return soliPends.size
    }
}
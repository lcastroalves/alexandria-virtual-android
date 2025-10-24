package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Comentario

class ComentarioAdapter(private val comentarios: List<Comentario>) :
    RecyclerView.Adapter<ComentarioAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgUsuario: ImageView = view.findViewById(R.id.imgUsuario)
        val txtNomeUsuario: TextView = view.findViewById(R.id.txtNomeUsuario)
        val txtComentario: TextView = view.findViewById(R.id.txtComentario)
        val txtEstrelas: TextView = view.findViewById(R.id.txtEstrelas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comentario, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comentario = comentarios[position]
        holder.imgUsuario.setImageResource(comentario.imagemRes)
        holder.txtNomeUsuario.text = comentario.nomeUsuario
        holder.txtComentario.text = comentario.comentario
        holder.txtEstrelas.text = "⭐".repeat(comentario.estrelas)
    }

    override fun getItemCount() = comentarios.size
}
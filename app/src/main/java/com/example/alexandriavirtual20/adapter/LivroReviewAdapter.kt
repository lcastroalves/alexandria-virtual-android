package com.example.alexandriavirtual20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class LivroReview(val titulo: String, val autor: String, val imagemRes: Int)

class LivroReviewAdapter(private val listaLivros: List<LivroReview>) :
    RecyclerView.Adapter<LivroReviewAdapter.LivroViewHolder>() {

    class LivroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgLivro: ImageView = view.findViewById(R.id.imgLivro)
        val txtTitulo: TextView = view.findViewById(R.id.txtTituloLivro)
        val txtAutor: TextView = view.findViewById(R.id.txtAutorLivro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro_review, parent, false)
        return LivroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = listaLivros[position]
        holder.txtTitulo.text = livro.titulo
        holder.txtAutor.text = livro.autor
        holder.imgLivro.setImageResource(livro.imagemRes)
    }

    override fun getItemCount(): Int = listaLivros.size
}

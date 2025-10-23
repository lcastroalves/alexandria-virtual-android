package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Livro

class LivroAdapterMeus(
    private val livros: List<Livro>,
    private val onClickAvaliacoes: (Livro) -> Unit = {}
) : RecyclerView.Adapter<LivroAdapterMeus.LivroViewHolder>() {

    inner class LivroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCapa: ImageView = view.findViewById(R.id.imgCapaLivro)
        val txtTitulo: TextView = view.findViewById(R.id.txtTituloLivro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro_meus, parent, false)
        return LivroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = livros[position]
        holder.imgCapa.setImageResource(livro.imagem)
        holder.txtTitulo.text = livro.titulo

        holder.itemView.setOnClickListener { onClickAvaliacoes(livro) }
    }

    override fun getItemCount(): Int = livros.size
}

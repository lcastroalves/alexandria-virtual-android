package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Livro

class LivroRetiradaAdapter(
    private val livros: List<Livro>
) : RecyclerView.Adapter<LivroRetiradaAdapter.LivroRetiradaViewHolder>() {

    inner class LivroRetiradaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCapa: ImageView = view.findViewById(R.id.imgCapaRetirada)
        val txtTitulo: TextView = view.findViewById(R.id.txtTituloRetirada)
        val txtAutor: TextView = view.findViewById(R.id.txtAutorRetirada)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroRetiradaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro_retirada, parent, false)
        return LivroRetiradaViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroRetiradaViewHolder, position: Int) {
        val livro = livros[position]

        holder.imgCapa.setImageResource(livro.imagem)
        holder.txtTitulo.text = livro.titulo
        holder.txtAutor.text = livro.autor
    }

    override fun getItemCount(): Int = livros.size
}
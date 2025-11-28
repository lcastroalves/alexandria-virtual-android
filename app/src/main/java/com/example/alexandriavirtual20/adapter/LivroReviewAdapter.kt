package com.example.alexandriavirtual20.adapter
// Note: Ajustei o pacote se você o moveu para 'adapter'

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Livro // Importa seu modelo Livro

class LivroReviewAdapter(private val listaLivros: List<Livro>) :
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

        // Implementação da capa Base64
        if (!livro.capa.isNullOrEmpty()) {
            try {
                val imageBytes = Base64.decode(livro.capa, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.imgLivro.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // Use uma imagem padrão se o Base64 estiver corrompido
                holder.imgLivro.setImageResource(R.drawable.no_image)
            }
        } else {
            // Use uma imagem padrão se a capa for nula/vazia
            holder.imgLivro.setImageResource(R.drawable.no_image)
        }
    }

    override fun getItemCount(): Int = listaLivros.size
}
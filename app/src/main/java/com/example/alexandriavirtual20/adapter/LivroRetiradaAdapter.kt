package com.example.alexandriavirtual20.adapter

import android.graphics.BitmapFactory
import android.util.Base64
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

        // ---------- CARREGAR IMAGEM BASE64 ----------
        if (!livro.capa.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(livro.capa, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.imgCapa.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.imgCapa.setImageResource(R.drawable.no_image)
            }
        } else {
            holder.imgCapa.setImageResource(R.drawable.no_image)
        }

        holder.txtTitulo.text = livro.titulo
        holder.txtAutor.text = livro.autor
    }

    override fun getItemCount(): Int = livros.size
}
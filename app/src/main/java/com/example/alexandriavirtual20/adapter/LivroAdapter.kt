package com.example.alexandriavirtual20.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Livro

class LivroAdapter(
    private val livros: List<Livro>,
    private val onClickAvaliacoes: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {

    private val selecionados = mutableSetOf<Livro>()

    inner class LivroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgLivro: ImageView = view.findViewById(R.id.imgLivro)
        val txtTitulo: TextView = view.findViewById(R.id.txtTitulo)
        val txtAutor: TextView = view.findViewById(R.id.txtAutor)
        val txtAvaliacoes: TextView = view.findViewById(R.id.txtAvaliacoes)
        val btnAvaliacoes: TextView = view.findViewById(R.id.btnAvaliacoes)
        val checkSelecionado: CheckBox = view.findViewById(R.id.checkSelecionado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro_emprestimo, parent, false)
        return LivroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = livros[position]

        // ---------- CARREGAR IMAGEM BASE64 ----------
        if (!livro.imageBase64.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(livro.imageBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.imgLivro.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.imgLivro.setImageResource(R.drawable.no_image)
            }
        } else {
            holder.imgLivro.setImageResource(R.drawable.no_image)
        }

        holder.txtTitulo.text = livro.titulo
        holder.txtAutor.text = livro.autor
        holder.txtAvaliacoes.text = "⭐ ${livro.avaliacoes} avaliações"

        holder.checkSelecionado.setOnCheckedChangeListener(null)
        holder.checkSelecionado.isChecked = selecionados.contains(livro)

        holder.checkSelecionado.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selecionados.add(livro)
            else selecionados.remove(livro)
        }

        holder.btnAvaliacoes.setOnClickListener {
            onClickAvaliacoes(livro)
        }
    }

    override fun getItemCount(): Int = livros.size

    fun getSelecionados(): List<Livro> = selecionados.toList()
}

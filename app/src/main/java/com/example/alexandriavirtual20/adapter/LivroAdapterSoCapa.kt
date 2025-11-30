package com.example.alexandriavirtual20.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Livro

class LivroAdapterSoCapa (
    private var livros: MutableList<Livro>,
    private val onLivroClick: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapterSoCapa.ViewHolder>() {


    //// cria o ViewHolder pegando a ImageView que representa a capa
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val capaLivro: ImageButton = view.findViewById(R.id.capaEvento)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_livro_so_capa, parent, false))
    }

    override fun onBindViewHolder(
        holder: LivroAdapterSoCapa.ViewHolder,
        position: Int
    ) {
        val livro = livros [position]

        if (!livro.capa.isNullOrEmpty()) {
            val imagemBytes = Base64.decode(livro.capa, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.size)
            holder.capaLivro.setImageBitmap(bitmap)
        } else {
            holder.capaLivro.setImageResource(R.drawable.livro3)
        }

        holder.capaLivro.setOnClickListener {
            onLivroClick(livro)
        }
    }

    override fun getItemCount(): Int {
        return livros.size
    }

    fun updateList(novaLista: List<Livro>) {
        livros = novaLista.toMutableList()
        notifyDataSetChanged()
    }


}
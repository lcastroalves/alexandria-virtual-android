package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Livro

class LivroAdapterFavoritos (
    private val livros : MutableList<Livro>,
    private val onInfoClick: (Livro) -> Unit,
    private val onReviewClick: (Livro) -> Unit,
    private val onFavotiroClick: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapterFavoritos.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val capaLivro : ImageView = view.findViewById(R.id.capaLivro)
        val titulo : TextView = view.findViewById(R.id.titulo)
        val subtitulo : TextView = view.findViewById(R.id.subtitulo)
        val qntAvaliac : TextView = view.findViewById(R.id.qntAvaliac)
        val autor: TextView = view.findViewById(R.id.autor)
        val btnFav : ImageView = view.findViewById(R.id.btnFav)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_livro_favoritos,parent,false))
    }

    override fun onBindViewHolder(holder: LivroAdapterFavoritos.ViewHolder, position: Int) {
        val livro = livros[position]

        holder.capaLivro.setImageResource(livro.imagem)
        holder.titulo.text = livro.titulo
        holder.subtitulo.text = livro.subtitulo
        holder.qntAvaliac.text = livro.avaliacao
        holder.autor.text = livro.autor

        if (livro.favorito) {
            holder.btnFav.setImageResource(R.drawable.coracao_azul_cheio)
        } else {
            holder.btnFav.setImageResource(R.drawable.coracao_azul_vazio)
        }

        // levar as telas de info do livro
        holder.capaLivro.setOnClickListener {
            onInfoClick(livro)
        }

        holder.titulo.setOnClickListener {
            onInfoClick(livro)
        }

        // levar a tela de review
        holder.qntAvaliac.setOnClickListener {
            onReviewClick(livro)
        }

        // muda o preenchimento do coração
        holder.btnFav.setOnClickListener {
            livro.favorito = !livro.favorito
            onFavotiroClick(livro)

            val posAtual = holder.adapterPosition
            if (posAtual != RecyclerView.NO_POSITION) {
                notifyItemChanged(posAtual)

                if (!livro.favorito) {
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        // ainda confirmado como não favorito?
                        if (!livro.favorito) {
                            livros.removeAt(posAtual)
                            notifyItemRemoved(posAtual)
                        }
                    }, 10_000) // 10 segundos
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return livros.size
    }
}
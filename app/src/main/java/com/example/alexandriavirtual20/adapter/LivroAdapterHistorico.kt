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

class LivroAdapterHistorico (
    private val livros : MutableList<Livro>,
    private val onInfoClick: (Livro) -> Unit,
    private val onReviewClick: (Livro) -> Unit,
    private val onFavotiroClick: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapterHistorico.ViewHolder>(){


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
    ): LivroAdapterHistorico.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_livro_historico,parent,false))
    }

    override fun onBindViewHolder(holder: LivroAdapterHistorico.ViewHolder, position: Int) {
        val livro = livros[position]

        // ---------- CARREGAR IMAGEM BASE64 ----------
        if (!livro.capa.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(livro.capa, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.capaLivro.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.capaLivro.setImageResource(R.drawable.no_image)
            }
        } else {
            holder.capaLivro.setImageResource(R.drawable.no_image)
        }

        holder.titulo.text = livro.titulo
        holder.qntAvaliac.text = livro.id
        holder.autor.text = livro.autor

        if (livro.favorito) {
            holder.btnFav.setImageResource(R.drawable.coracao_cheio)
        } else {
            holder.btnFav.setImageResource(R.drawable.coracao_vazio)
        }

        // levar as telas de info do livro (define mesmo só no kt da telaHistorico)
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
            livro.favorito = !livro.favorito          // troca o estado
            notifyItemChanged(position)              // atualiza só o item
            onFavotiroClick(livro)                   // callback se quiser salvar no banco
        }
    }

    override fun getItemCount(): Int {
        return livros.size
    }


}
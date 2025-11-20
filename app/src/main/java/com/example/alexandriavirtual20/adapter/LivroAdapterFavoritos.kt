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

class LivroAdapterFavoritos(
    private val livros: MutableList<Livro>,
    private val onInfoClick: (Livro) -> Unit,
    private val onReviewClick: (Livro) -> Unit,
    private val onFavoritoClick: (Livro) -> Unit
) : RecyclerView.Adapter<LivroAdapterFavoritos.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val capaLivro: ImageView = view.findViewById(R.id.capaLivro)
        val titulo: TextView = view.findViewById(R.id.titulo)
        val subtitulo: TextView = view.findViewById(R.id.subtitulo)
        val qntAvaliac: TextView = view.findViewById(R.id.qntAvaliac)
        val autor: TextView = view.findViewById(R.id.autor)
        val btnFav: ImageView = view.findViewById(R.id.btnFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.u_cardview_livro_favoritos, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val livro = livros[position]

        // ---------- CARREGAR IMAGEM BASE64 ----------
        if (!livro.imageBase64.isNullOrEmpty()) {
            try {
                val bytes = Base64.decode(livro.imageBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.capaLivro.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.capaLivro.setImageResource(R.drawable.no_image)
            }
        } else {
            holder.capaLivro.setImageResource(R.drawable.no_image)
        }

        holder.titulo.text = livro.titulo
        holder.autor.text = livro.autor

        // ⭐ Exibir avaliação corretamente
        holder.qntAvaliac.text = "⭐ ${livro.avaliacoes} avaliações"

        // Atualiza coração favorito
        holder.btnFav.setImageResource(
            if (livro.favorito)
                R.drawable.coracao_azul_cheio
            else
                R.drawable.coracao_azul_vazio
        )

        // Abre tela de informações
        holder.capaLivro.setOnClickListener { onInfoClick(livro) }
        holder.titulo.setOnClickListener { onInfoClick(livro) }

        // Abre tela de reviews
        holder.qntAvaliac.setOnClickListener { onReviewClick(livro) }

        // Toggle do favorito
        holder.btnFav.setOnClickListener {
            livro.favorito = !livro.favorito
            onFavoritoClick(livro)

            val posAtual = holder.adapterPosition
            if (posAtual != RecyclerView.NO_POSITION) {
                notifyItemChanged(posAtual)

                // Remove da lista após 10s se continuar desfavoritado
                if (!livro.favorito) {
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        if (!livro.favorito && posAtual < livros.size) {
                            livros.removeAt(posAtual)
                            notifyItemRemoved(posAtual)
                        }
                    }, 10_000)
                }
            }
        }
    }

    override fun getItemCount(): Int = livros.size
}

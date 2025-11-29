package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Comentario

class ComentarioAdapter(private val comentarios: List<Comentario>) :
    RecyclerView.Adapter<ComentarioAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgUsuario: ImageView = view.findViewById(R.id.imgUsuario)
        val txtNomeUsuario: TextView = view.findViewById(R.id.txtNomeUsuario)
        val txtComentario: TextView = view.findViewById(R.id.txtComentario)
        // 🌟 Se você estiver usando 5 ImageViews separadas para estrelas,
        // você precisará de mais IDs de Views aqui (como sugeri no tópico anterior).
        // Se usar apenas uma TextView, este ID deve estar correto:
        val txtEstrelas: TextView = view.findViewById(R.id.txtEstrelas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comentario, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comentario = comentarios[position]

        // --- 1. Imagem do Usuário / Avatar ---
        // 🌟 Define um avatar padrão se fotoRes for null ou 0.
        if (comentario.fotoRes != null && comentario.fotoRes != 0) {
            holder.imgUsuario.setImageResource(comentario.fotoRes!!)
        } else {
            // Assumindo que você tem um drawable de usuário padrão
            holder.imgUsuario.setImageResource(R.drawable.imagem_do_usuario_com_fundo_preto)
        }

        // --- 2. Nome do Usuário ---
        // 🌟 Usa o ID do usuário (parcial) se o nome não estiver disponível.
        holder.txtNomeUsuario.text = comentario.nomeUsuario ?: "Usuário (ID: ${comentario.idUsu.take(4)}...)"

        // --- 3. Comentário ---
        holder.txtComentario.text = comentario.comentario ?: "Comentário não fornecido."

        // --- 4. Exibição das Estrelas ---
        val nota = comentario.estrelas

        if (nota in 1..5) {
            // Exibe a nota com emojis de estrela
            holder.txtEstrelas.text = "⭐".repeat(nota)
        } else {
            // Caso a nota seja 0 ou inválida
            holder.txtEstrelas.text = "Sem nota"
        }
    }

    override fun getItemCount(): Int = comentarios.size
}
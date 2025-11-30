package com.example.alexandriavirtual20.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R

data class Solicitacao(
    val idEmprestimo: String,
    val titulo: String,
    val autor: String,
    val usuario: String,
    val email: String,
    val data: String,
    val prazo: String,
    val local: String,
    val capa: String   // Base64
)

class SolicitacaoAdapter(
    private val lista: List<Solicitacao>,
    private val onAutorizar: (position: Int) -> Unit,
    private val onRecusar: (position: Int) -> Unit
) : RecyclerView.Adapter<SolicitacaoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgLivro: ImageView = view.findViewById(R.id.imgLivro)
        val txtTitulo: TextView = view.findViewById(R.id.txtTituloLivro)
        val txtAutor: TextView = view.findViewById(R.id.txtAutor)
        val txtUsuario: TextView = view.findViewById(R.id.txtUsuario)
        val txtEmail: TextView = view.findViewById(R.id.txtEmail)
        val txtPrazo: TextView = view.findViewById(R.id.txtPrazo)
        val txtLocal: TextView = view.findViewById(R.id.txtLocal)
        val btnAutorizar: Button = view.findViewById(R.id.btnAutorizar)
        val btnRecusar: Button = view.findViewById(R.id.btnRecusar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_solicitacao_pendente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        // ------------ IMAGEM BASE64 ---------------
        if (item.capa.isNotEmpty()) {
            try {
                val imageBytes = Base64.decode(item.capa, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                holder.imgLivro.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.imgLivro.setImageResource(R.drawable.no_image)
            }
        } else {
            holder.imgLivro.setImageResource(R.drawable.no_image)
        }

        holder.txtTitulo.text = item.titulo
        holder.txtAutor.text = "Autor: ${item.autor}"
        holder.txtUsuario.text = "Usuário: ${item.usuario}"
        holder.txtEmail.text = "Email: ${item.email}"
        holder.txtPrazo.text = "Data limite: ${item.prazo}"
        holder.txtLocal.text = "Local: ${item.local}"

        holder.btnAutorizar.setOnClickListener { onAutorizar(holder.adapterPosition) }
        holder.btnRecusar.setOnClickListener { onRecusar(holder.adapterPosition) }
    }

    override fun getItemCount() = lista.size
}

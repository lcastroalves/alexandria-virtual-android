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
import java.text.SimpleDateFormat
import java.util.Locale

data class Solicitacao(
    val idEmprestimo: String,
    val idLivro: String,
    val idUsuario: String,
    val titulo: String,
    val autor: String,
    val usuario: String,
    val email: String,
    val dataSolicitacao: Long, // ⬅ timestamp vindo do Firestore
    val prazo: String,
    val local: String,
    val capa: String
)

class SolicitacaoAdapter(
    private val lista: MutableList<Solicitacao>,
    private val onAutorizar: (Solicitacao) -> Unit,
    private val onRecusar: (Solicitacao) -> Unit
) : RecyclerView.Adapter<SolicitacaoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgLivro: ImageView = view.findViewById(R.id.imgLivro)
        val txtData: TextView = view.findViewById(R.id.txtData)
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

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        // === IMAGEM BASE64 ===
        if (item.capa.isNotEmpty()) {
            try {
                val bytes = Base64.decode(item.capa, Base64.DEFAULT)
                holder.imgLivro.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
            } catch (_: Exception) {
                holder.imgLivro.setImageResource(R.drawable.no_image)
            }
        } else {
            holder.imgLivro.setImageResource(R.drawable.no_image)
        }

        // === FORMATAR DATA ===
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dataFormatada = sdf.format(item.dataSolicitacao)

        holder.txtData.text = dataFormatada
        holder.txtTitulo.text = item.titulo
        holder.txtAutor.text = "Autor: ${item.autor}"
        holder.txtUsuario.text = "Usuário: ${item.usuario}"
        holder.txtEmail.text = "Email: ${item.email}"
        holder.txtPrazo.text = "Prazo: ${item.prazo}"
        holder.txtLocal.text = "Local: ${item.local}"

        holder.btnAutorizar.setOnClickListener { onAutorizar(item) }
        holder.btnRecusar.setOnClickListener { onRecusar(item) }
    }

    fun removerItem(item: Solicitacao) {
        val index = lista.indexOf(item)
        if (index != -1) {
            lista.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}

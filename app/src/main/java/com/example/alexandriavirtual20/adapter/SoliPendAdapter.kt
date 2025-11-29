package com.example.alexandriavirtual20.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R

data class SoliPend(
    val titulo: String,
    val autor: String,
    val data: String,
    val prazo: String,
    val local: String,
    val imagem: String,  // agora é Base64
    var pendente: Boolean
)

class SoliPendAdapter(
    private var soliPends: MutableList<SoliPend>
) : RecyclerView.Adapter<SoliPendAdapter.ViewHolder>() {

    private var listaFiltrada = soliPends.toMutableList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagem: ImageView = view.findViewById(R.id.imagemSoliPend)
        val nome: TextView = view.findViewById(R.id.nomeSoliPend)
        val autor: TextView = view.findViewById(R.id.autorSoliPend)
        val pendenteImagem: ImageView = view.findViewById(R.id.pendenteSoliPend)
        val btnOk: Button = view.findViewById(R.id.botaoSoliPend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.u_cardview_solicitacoes_pend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val soli = listaFiltrada[position]

        // ------------------------------
        // IMAGEM BASE64 → BITMAP
        // ------------------------------
        if (soli.imagem.isNotEmpty()) {
            try {
                val bytes = Base64.decode(soli.imagem, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.imagem.setImageBitmap(bitmap)
            } catch (e: Exception) {
                holder.imagem.setImageResource(R.drawable.no_image)
            }
        } else {
            holder.imagem.setImageResource(R.drawable.no_image)
        }

        // Texto
        holder.nome.text = soli.titulo
        holder.autor.text = soli.autor

        // Situação do empréstimo
        if (soli.pendente) {
            holder.pendenteImagem.setImageResource(R.drawable.relogio)
            holder.btnOk.visibility = INVISIBLE
        } else {
            holder.pendenteImagem.setImageResource(R.drawable.negado)
            holder.btnOk.visibility = View.VISIBLE

            holder.btnOk.setOnClickListener {
                removerSoliPend(soli)
            }
        }
    }

    override fun getItemCount(): Int = listaFiltrada.size

    fun updateList(list: MutableList<SoliPend>) {
        this.soliPends = list
        this.listaFiltrada = list.toMutableList()
        notifyDataSetChanged()
    }

    fun removerSoliPend(item: SoliPend) {
        val index = listaFiltrada.indexOf(item)
        if (index != -1) {
            listaFiltrada.removeAt(index)
            soliPends.remove(item)
            notifyItemRemoved(index)
        }
    }

    fun filtrar(texto: String) {
        val t = texto.lowercase()

        listaFiltrada = if (t.isEmpty()) {
            soliPends.toMutableList()
        } else {
            soliPends.filter {
                it.titulo.lowercase().contains(t) ||
                        it.autor.lowercase().contains(t)
            }.toMutableList()
        }

        notifyDataSetChanged()
    }
}

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
    val idEmprestimo: String,
    val titulo: String,
    val autor: String,
    val data: String,
    val prazo: String,
    val local: String,
    val imagem: String,
    var pendente: Boolean,
    val avaliacao: Double = 0.0,
    val qtdAvaliacoes: Int = 0
)


class SoliPendAdapter(
    private var soliPends: MutableList<SoliPend>,
    private val onDeleteClick: (SoliPend) -> Unit
) : RecyclerView.Adapter<SoliPendAdapter.ViewHolder>() {

    private var listaFiltrada = soliPends.toMutableList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagem: ImageView = view.findViewById(R.id.imagemSoliPend)
        val nome: TextView = view.findViewById(R.id.nomeSoliPend)
        val autor: TextView = view.findViewById(R.id.autorSoliPend)
        val avaliacaoText: TextView = view.findViewById(R.id.avaliacaoSoliPend)
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

        // IMAGEM
        if (soli.imagem.isNotEmpty()) {
            try {
                val bytes = Base64.decode(soli.imagem, Base64.DEFAULT)
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                holder.imagem.setImageBitmap(bmp)
            } catch (e: Exception) {
                holder.imagem.setImageResource(R.drawable.no_image)
            }
        } else holder.imagem.setImageResource(R.drawable.no_image)

        // TEXTO
        holder.nome.text = soli.titulo
        holder.autor.text = soli.autor

        // ⭐ AVALIAÇÃO — EXATAMENTE COMO NO LIVROADAPTER
        val textoAvaliacao = when (soli.qtdAvaliacoes) {
            0 -> "Sem avaliações"
            1 -> "⭐ ${soli.avaliacao} • 1 avaliação"
            else -> "⭐ ${soli.avaliacao} • ${soli.qtdAvaliacoes} avaliações"
        }

        holder.avaliacaoText.text = textoAvaliacao

        // SITUAÇÃO
        if (soli.pendente) {
            holder.pendenteImagem.setImageResource(R.drawable.relogio)
            holder.btnOk.visibility = View.INVISIBLE
        } else {
            holder.pendenteImagem.setImageResource(R.drawable.negado)
            holder.btnOk.visibility = View.VISIBLE

            holder.btnOk.setOnClickListener {
                onDeleteClick(soli)
            }
        }
    }

    override fun getItemCount(): Int = listaFiltrada.size

    fun updateList(list: MutableList<SoliPend>) {
        soliPends = list
        listaFiltrada = list.toMutableList()
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


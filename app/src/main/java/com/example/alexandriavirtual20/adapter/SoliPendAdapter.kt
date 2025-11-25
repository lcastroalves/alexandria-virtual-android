package com.example.alexandriavirtual20.adapter

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
    val usuario: String,
    val email: String,
    val data: String,
    val prazo: String,
    val local: String,
    val imagem: Int,
    var pendente : Boolean
)

class SoliPendAdapter (
    private val soliPends: MutableList<SoliPend>,
    private val onExcluirClick: ((SoliPend) -> Unit)? = null
) :
    RecyclerView.Adapter<SoliPendAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagem: ImageView = view.findViewById(R.id.imagemSoliPend)
        val nome: TextView = view.findViewById(R.id.nomeSoliPend)
        val autor: TextView = view.findViewById(R.id.autorSoliPend)
        val avaliacao: TextView = view.findViewById(R.id.avaliacaoSoliPend)
        val subtitulo: TextView = view.findViewById(R.id.subtituloSoliPend)

        val pendenteImagem: ImageView = view.findViewById(R.id.pendenteSoliPend)
        val btnOk: Button = view.findViewById(R.id.botaoSoliPend)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_solicitacoes_pend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val soliPend = soliPends[position]

        holder.imagem.setImageResource(soliPend.imagem)
        holder.nome.text = soliPend.titulo
        holder.autor.text = soliPend.autor
//        holder.avaliacao.text = soliPend.avaliacao
//        holder.subtitulo.text = soliPend.subtitulo


        if (soliPend.pendente) {
            holder.pendenteImagem.setImageResource(R.drawable.relogio)

            holder.btnOk.visibility = INVISIBLE

        }
        else {
            holder.pendenteImagem.setImageResource(R.drawable.negado)

            holder.btnOk.visibility = View.VISIBLE

            holder.btnOk.setOnClickListener {
                removerSoliPend(soliPend)
            }

        }

        holder.btnOk.setOnClickListener {
            onExcluirClick?.invoke(soliPend)
        }

    }

    fun removerSoliPend(soliPend: SoliPend) {
        val position = soliPends.indexOf(soliPend)
        if (position > -1) {
            soliPends.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun getItemCount(): Int {
        return soliPends.size
    }
}

package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Evento

class ListaEventoAdmAdapter (
    private val eventos: MutableList<Evento>,
    private val btnEditarClick: (Evento) -> Unit
) :
    RecyclerView.Adapter<ListaEventoAdmAdapter.ViewHolder>(){
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val imagem: ImageView = view.findViewById(R.id.imagemEventoListaAdm)
        val nome: TextView = view.findViewById(R.id.nomeEventoListaAdm)
        val horario: TextView = view.findViewById(R.id.horarioEventoListaAdm)
        val btnEditar: ImageButton = view.findViewById(R.id.botaoEditarEventoListaAdm)
        val checkBox: CheckBox = view.findViewById(R.id.checkBoxEventoListaAdm)
    }

    // infla o layout que contem os elementos
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListaEventoAdmAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_eventos_adm, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evento = eventos[position]

        holder.imagem.setImageResource(evento.imagem)
        holder.nome.text = evento.nome
        holder.horario.text = evento.horario

        // para a checkbox. clicar duas vezes
        holder.checkBox.isChecked = evento.isSelected
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            evento.isSelected = isChecked
        }

        holder.btnEditar.setOnClickListener {
            btnEditarClick(evento)
        }

    }

    fun excluirSelecionados(){
        eventos.removeAll { it.isSelected }        // remove todos que retornar isSelected como true
        notifyDataSetChanged()                      // Depois que a lista muda, o Adapter precisa ser avisado para redesenhar a tela
    }

    override fun getItemCount(): Int {
        return eventos.size
    }

    companion object

}

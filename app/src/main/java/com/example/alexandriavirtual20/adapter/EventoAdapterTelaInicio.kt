package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Evento

class EventoAdapterTelaInicio (
    private val eventos: MutableList<Evento>,
    private val onEventoClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventoAdapterTelaInicio.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraintLayout)
        val capaEvento: ImageView = view.findViewById(R.id.capaEvento)
        val titulo : TextView = view.findViewById(R.id.titulo)
        val data : TextView = view.findViewById(R.id.data)
        val horario : TextView = view.findViewById(R.id.horario)
        val local : TextView = view.findViewById(R.id.local)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventoAdapterTelaInicio.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_eventos_inicio, parent, false))
    }

    override fun onBindViewHolder(
        holder: EventoAdapterTelaInicio.ViewHolder,
        position: Int
    ) {
        val evento = eventos[position]

        holder.capaEvento.setImageResource((evento.imagem).toInt())
        holder.titulo.text = evento.nome
        holder.data.text = evento.data
        holder.horario.text = evento.horario
        holder.local.text = evento.local

        holder.constraintLayout.setOnClickListener {
            onEventoClick(evento)
        }

    }

    override fun getItemCount(): Int {
        return eventos.size
    }


}
//package com.example.alexandriavirtual20.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.alexandriavirtual20.R
//import com.example.alexandriavirtual20.model.Evento
//
//class ListaEventoAdapter (
//    private val eventos: MutableList<Evento>
//) :
//    RecyclerView.Adapter<NotificacaoAdapter.ViewHolder>() {
//
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        val imagem: ImageView = view.findViewById(R.id.imagemNotificacao)
//        val nome: TextView = view.findViewById(R.id.nome)
//        val data: TextView = view.findViewById(R.id.data)
//        val mensagem: TextView = view.findViewById(R.id.mensagem)
//        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluir)
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): ViewHolder {
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_evento_da_lista, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: EventoAdapter.ViewHolder, position: Int) {
//        val evento = eventos[position]
//
//        holder.imagem.setImageResource(evento.imagem)
//        holder.nome.text = evento.nome
//        holder.tipo.text = evento.tipo
//        holder.data.text = .data
//        holder.mensagem.text = notificacao.mensagem
//
//        holder.btnExcluir.setOnClickListener {
//            onExcluirClick?.invoke(notificacao)
//        }
//    }
//
//    fun removerNotificacao(notificacao: Notificacao) {
//        val position = notificacoes.indexOf(notificacao)
//        if (position > -1) {
//            notificacoes.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, itemCount)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return notificacoes.size
//    }
//
//}
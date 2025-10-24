//package com.example.alexandriavirtual20.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CheckBox
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.alexandriavirtual20.R
//import com.example.alexandriavirtual20.model.Evento
//
//class ListaEventoAdmAdapter (
//    private val eventos: MutableList<Evento>,
//    private val btnEditarClick: (Evento) -> Unit
//) :
//    RecyclerView.Adapter<ProdutoAdapter.ViewHolder>(){
//    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
//        val imagem: ImageView = view.findViewById(R.id.imagemEventoListaAdm)
//        val nome: TextView = view.findViewById(R.id.nomeEventoListaAdm)
//        val horario: TextView = view.findViewById(R.id.horarioEventoListaAdm)
//        val btnEditar: ImageButton = view.findViewById(R.id.botaoEditarEventoListaAdm)
//        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
//    }
//
//    // infla o layout que contem os elementos
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): ListaEventoAdmAdapter.ViewHolder {
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_add_evento, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val evento = eventos[position]
//
//        holder.capaLivro.setImageResource(evento.imageRes)
//        holder.titulo.text = evento.titulo
//        holder.autor.text = evento.autor
//
//        // para a checkbox. clicar duas vezes
//        holder.checkBox.isChecked = produto.isSelected
//        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
//            produto.isSelected = isChecked
//        }
//
//        holder.btnEditar.setOnClickListener {
//            btnEditarClick(produto)
//        }
//
//    }
//
//    fun excluirSelecionados(){
//        produtos.removeAll { it.isSelected }        // remove todos que retornar isSelected como true
//        notifyDataSetChanged()                      // Depois que a lista muda, o Adapter precisa ser avisado para redesenhar a tela
//    }
//
//    override fun getItemCount(): Int {
//        return produtos.size
//    }
//
//    companion object
//
//}

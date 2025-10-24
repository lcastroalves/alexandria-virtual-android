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
import com.example.alexandriavirtual20.model.Produto

class ProdutoAdapter (
    private val produtos: MutableList<Produto>,
    private val btnEditarClick: (Produto) -> Unit            // uma função que recebe um Produto e não retorna nada. quem criar o adapter decide o que acontece ao clicar no botão de editar
) :
    RecyclerView.Adapter<ProdutoAdapter.ViewHolder>(){
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val capaLivro: ImageView = view.findViewById(R.id.capaLivro)
        val titulo: TextView = view.findViewById(R.id.titulo)
        val autor: TextView = view.findViewById(R.id.autor)
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditarProd)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
    }

    // infla o layout que contem os elementos
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_produto, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]

        holder.capaLivro.setImageResource(produto.imageRes)
        holder.titulo.text = produto.titulo
        holder.autor.text = produto.autor

        // para a checkbox. clicar duas vezes
        holder.checkBox.isChecked = produto.isSelected
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            produto.isSelected = isChecked
        }

        holder.btnEditar.setOnClickListener {
            btnEditarClick(produto)
        }

    }

    fun excluirSelecionados(){
        produtos.removeAll { it.isSelected }        // remove todos que retornar isSelected como true
        notifyDataSetChanged()                      // Depois que a lista muda, o Adapter precisa ser avisado para redesenhar a tela
    }

    override fun getItemCount(): Int {
        return produtos.size
    }

}

package com.example.alexandriavirtual20.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
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
    private val btnEditarClick: (Produto) -> Unit
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

        holder.titulo.text = produto.titulo
        holder.autor.text = produto.autor

        // Decodifica imagem base64
        if (produto.imageBase64.isNotEmpty()) {
            val imagemBytes = Base64.decode(produto.imageBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.size)
            holder.capaLivro.setImageBitmap(bitmap)
        } else {
            holder.capaLivro.setImageResource(R.drawable.iconelivro)
        }

        // para a checkbox. clicar duas vezes
        holder.checkBox.setOnCheckedChangeListener(null)   //para limpar e evitar comportamento incorreto
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

    fun atualizarLista(novaLista: MutableList<Produto>) {
        Log.d("ADAPTER", "Atualizando lista com ${novaLista.size} itens")
        produtos.clear()
        produtos.addAll(novaLista)
        notifyDataSetChanged()
    }

    fun getSelecionados(): List<Produto> {
        return produtos.filter { it.isSelected }
    }



}

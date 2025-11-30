// HistoricoEmprestimoAdapter.kt
package com.example.alexandriavirtual20.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.HistoricoEmprestimo

class HistoricoEmprestimoAdapter(
    private val historico: List<HistoricoEmprestimo>,
    // Ações de Devolução/Atraso no Histórico
    private val onDevolver: (HistoricoEmprestimo) -> Unit,
    private val onAtrasar: (HistoricoEmprestimo) -> Unit
) : RecyclerView.Adapter<HistoricoEmprestimoAdapter.HistoricoViewHolder>() {

    // 💡 ATENÇÃO: Os IDs devem corresponder ao seu XML de item de histórico (Ex: adm_item_historico_emprestimo.xml)
    inner class HistoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Dados do Responsável (caixa superior)
        val txtNomeResponsavel: TextView = itemView.findViewById(R.id.txtNomeResponsavel) // Nome do Responsável
        val txtEmailResponsavel: TextView = itemView.findViewById(R.id.txtEmailResponsavel) // Email do Responsável

        // Dados do Livro
        val imgCapa: ImageView = itemView.findViewById(R.id.imgCapa)
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val txtAutor: TextView = itemView.findViewById(R.id.txtAutor)

        // Datas e Status (embaixo da capa)
        val txtDataRetirada: TextView = itemView.findViewById(R.id.txtDataRetirada) // Corresponde ao "09 de Set" no design
        val txtSituacaoDevolucao: TextView = itemView.findViewById(R.id.txtSituacaoDevolucao) // Corresponde ao "Em dia"

        // Botões (Assumindo que você os adicionará no layout de Histórico)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adm_item_historico_emprestimo, parent, false) // 🌟 Usando um novo nome de layout
        return HistoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        val item = historico[position]

        // 1. Mapeamento dos Dados do Responsável (Caixa Superior)
        holder.txtNomeResponsavel.text = "Usuário: ${item.nomeUsuario}"
        holder.txtEmailResponsavel.text = "Email: ${item.emailUsuario}"

        // 2. Mapeamento dos Dados do Livro
        holder.txtTitulo.text = item.titulo
        holder.txtAutor.text = "Autor: ${item.autor}"

        // 3. Mapeamento das Datas e Status
        // O design "09 de Set" provavelmente representa a data de retirada.
        holder.txtDataRetirada.text = item.dataEmprestimo
        holder.txtSituacaoDevolucao.text = item.statusDevolucao

        if (item.capa.isNotEmpty()) {
            try {
                // Decodifica a string Base64 para um array de bytes
                val imageBytes = Base64.decode(item.capa, Base64.DEFAULT)
                // Converte o array de bytes em um Bitmap
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                // Define a imagem no ImageView
                holder.imgCapa.setImageBitmap(decodedImage)
            } catch (e: IllegalArgumentException) {
                // Logar o erro se a string Base64 for inválida
                Log.e("Adapter", "Erro ao decodificar Base64: ${e.message}")
                holder.imgCapa.setImageResource(R.drawable.no_image) // 💡 Substitua pelo seu placeholder
            }
        } else {
            // Caso a string de capa esteja vazia ou nula
            holder.imgCapa.setImageResource(R.drawable.no_image) // 💡 Substitua pelo seu placeholder
        }



        // 5. Configuração dos Botões




        // Lógica de esconder botões se já estiver devolvido

    }

    override fun getItemCount(): Int = historico.size
}
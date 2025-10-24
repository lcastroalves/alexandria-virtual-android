package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Usuario
import com.google.android.material.imageview.ShapeableImageView

class UsuarioAdapter(
    private val onEditar: (Usuario) -> Unit,
    private val onCheckedChange: (Usuario, Boolean) -> Unit = { _, _ -> }
) : ListAdapter<Usuario, UsuarioAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Usuario>() {
        override fun areItemsTheSame(a: Usuario, b: Usuario) = a.id == b.id
        override fun areContentsTheSame(a: Usuario, b: Usuario) = a == b
    }

    private var selecionados = mutableSetOf<Long>()

    inner class VH(view: View) : RecyclerView.ViewHolder(view){
        private val imgFoto: ShapeableImageView = view.findViewById(R.id.imagemPerfil)
        private val tvNome: TextView = view.findViewById(R.id.nomeUsu)
        private val btnEditar: ImageButton = view.findViewById(R.id.btnEdit)
        private val check: AppCompatCheckBox = view.findViewById(R.id.checkSelecionado)

        fun bind(item: Usuario) {
            imgFoto.setImageResource(item.fotoPerfil)
            tvNome.text = item.nome

            check.setOnCheckedChangeListener(null)
            check.isChecked = item.id in selecionados

            btnEditar.setOnClickListener {
                onEditar(item)
            }

            check.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selecionados += item.id else selecionados -= item.id
                onCheckedChange(item, isChecked)
            }

            itemView.setOnClickListener {
                val novo = !(item.id in selecionados)
                check.isChecked = novo
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_usu_cadast, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelecionados(): List<Usuario> =
        currentList.filter {
            it.id in selecionados
        }

    fun clearSelecao() {
        selecionados.clear()
        notifyDataSetChanged()
    }
}

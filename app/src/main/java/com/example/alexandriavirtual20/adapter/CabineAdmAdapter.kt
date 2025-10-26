package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.CabineAdm

class CabineAdmAdapter() : ListAdapter<CabineAdm, CabineAdmAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<CabineAdm>() {
        override fun areItemsTheSame(oldItem: CabineAdm, newItem: CabineAdm) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CabineAdm, newItem: CabineAdm) = oldItem == newItem
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val foto: ImageView = view.findViewById(R.id.fotoPerfil2)
        private val tvNome: TextView = view.findViewById(R.id.tvCabineNome)
        private val tvCab: TextView = view.findViewById(R.id.tvCabineBloco)

        fun bind(item: CabineAdm) {
            foto.setImageResource(item.iconRes)
            tvCab.text = item.cabine
            tvNome.text = item.nome
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.u_cardview_cabines_adm, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}

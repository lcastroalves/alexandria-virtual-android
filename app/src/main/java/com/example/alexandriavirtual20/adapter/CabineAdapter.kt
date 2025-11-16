package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Cabine

class CabineAdapter(
    private val onReservarClick: (Cabine) -> Unit
) : ListAdapter<Cabine, CabineAdapter.VH>(Diff) {
    object Diff : DiffUtil.ItemCallback<Cabine>() {
        override fun areItemsTheSame(a: Cabine, b: Cabine) = a.id == b.id
        override fun areContentsTheSame(a: Cabine, b: Cabine) = a == b
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view)  {
        val icon = view.findViewById<ImageView>(R.id.iconCabine)
        private val numero = view.findViewById<TextView>(R.id.tvCabineNome)
        private val reservar = view.findViewById<MaterialButton>(R.id.btnReservar)

        fun bind(item: Cabine) {
            numero.text = "Cabine ${item.numero}"

            reservar.setOnClickListener {
                onReservarClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.u_cardview_cabines, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position))
}
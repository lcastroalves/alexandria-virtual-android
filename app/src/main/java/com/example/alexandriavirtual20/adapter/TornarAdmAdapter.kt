package com.example.alexandriavirtual20.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Usuario

class TornarAdmAdapter (
    private val usuarios: MutableList<Usuario>,
    private val onClick: ((Usuario) -> Unit)? = null
) :
    RecyclerView.Adapter<TornarAdmAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagem: ImageView = view.findViewById(R.id.imagemAddAdm)
        val nome: TextView = view.findViewById(R.id.nomeAddAdm)
        val btnTornarAdm: Button = view.findViewById(R.id.btnTornarAdm)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.u_cardview_add_adm, parent, false))
    }

    override fun onBindViewHolder(holder: TornarAdmAdapter.ViewHolder, position: Int) {
        val usuario = usuarios[position]

        holder.imagem.setImageResource(usuario.fotoPerfil)
        holder.nome.text = usuario.nome

        holder.btnTornarAdm.setOnClickListener {
            onClick?.invoke(usuario)
        }
    }

    fun tornarAdm(usuario: Usuario) {
        val position = usuarios.indexOf(usuario)
        if (position > -1) {
            usuarios.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

}
package com.example.alexandriavirtual20.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Cabine
import android.util.Base64

class CabineAdmAdapter(
    private val buscarFotoAluno: (String, (String?) -> Unit) -> Unit
) : ListAdapter<Cabine, CabineAdmAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Cabine>() {
        override fun areItemsTheSame(a: Cabine, b: Cabine) = a.id == b.id
        override fun areContentsTheSame(a: Cabine, b: Cabine) = a == b
    }

    private val fotoCache = object : LruCache<String, Bitmap>(4 * 1024 * 1024) {
        override fun sizeOf(key: String, value: Bitmap): Int = value.byteCount
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val foto: ImageView = view.findViewById(R.id.fotoPerfil2)
        private val tvNumero: TextView = view.findViewById(R.id.tvCabineNome)
        private val tvNome: TextView = view.findViewById(R.id.tvCabineBloco)

        fun bind(item: Cabine) {
            tvNome.text = item.aluno
            tvNumero.text = "Cabine ${item.numero}"

            val cacheKey = item.aluno

            val bmpFromCache = fotoCache.get(cacheKey)
            if (bmpFromCache != null) {
                foto.setImageBitmap(bmpFromCache)
                return
            }

            foto.setImageResource(R.drawable.empty_user)

            buscarFotoAluno(item.aluno) { fotoBase64 ->
                if (fotoBase64.isNullOrBlank()) {
                    foto.setImageResource(R.drawable.empty_user)
                } else {
                    val bmp = decodeBase64Safe(fotoBase64)
                    if (bmp != null) {
                        fotoCache.put(cacheKey, bmp)
                        if (bindingAdapterPosition != RecyclerView.NO_POSITION &&
                            bindingAdapterPosition < currentList.size &&
                            currentList[bindingAdapterPosition].aluno == item.aluno
                        ) {
                            foto.setImageBitmap(bmp)
                        }
                    } else {
                        foto.setImageResource(R.drawable.empty_user)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.u_cardview_cabines_adm, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: CabineAdmAdapter.VH, position: Int) =
        holder.bind(getItem(position))
    private fun decodeBase64Safe(b64: String): Bitmap? = try {
        val bytes = Base64.decode(b64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (_: Throwable) {
        null
    }

}

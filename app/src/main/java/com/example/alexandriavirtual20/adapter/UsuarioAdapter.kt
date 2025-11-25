package com.example.alexandriavirtual20.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Usuario
import android.util.Base64

class UsuarioAdapter(
    private val onEditar: (Usuario) -> Unit,
    private val onCheckedChange: (Usuario, Boolean) -> Unit = { _, _ -> }
) : ListAdapter<Usuario, UsuarioAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Usuario>() {
        override fun areItemsTheSame(a: Usuario, b: Usuario) = a.id == b.id
        override fun areContentsTheSame(a: Usuario, b: Usuario) = a == b
    }

    private val fotoCache = object : LruCache<String, Bitmap>(/* ~4MB aprox */ 4 * 1024 * 1024) {
        override fun sizeOf(key: String, value: Bitmap): Int = value.byteCount
    }

    private var selecionados = mutableSetOf<String>()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = getItem(position).id.hashCode().toLong()

    inner class VH(view: View) : RecyclerView.ViewHolder(view){
        private val imgFoto: ImageView = view.findViewById(R.id.imagemPerfil)
        private val tvNome: TextView = view.findViewById(R.id.nomeUsu)
        private val btnEditar: ImageButton = view.findViewById(R.id.btnEdit)
        private val check: CheckBox = view.findViewById(R.id.checkBox2)
        fun bind(item: Usuario) {
            tvNome.text = item.nome

            val cacheKey = item.id
            val bmpFromCache = fotoCache.get(cacheKey)

            when {
                bmpFromCache != null -> imgFoto.setImageBitmap(bmpFromCache)
                !item.fotoPerfil.isNullOrBlank() -> {
                    val bmp = decodeBase64Safe(item.fotoPerfil)
                    if (bmp != null) {
                        fotoCache.put(cacheKey, bmp)
                        imgFoto.setImageBitmap(bmp)
                    } else {
                        imgFoto.setImageResource(R.drawable.empty_user) // fallback
                    }
                }
                else -> imgFoto.setImageResource(R.drawable.empty_user) // sem foto
            }

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
                check.performClick()
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

    private fun decodeBase64Safe(b64: String): Bitmap? = try {
        val bytes = Base64.decode(b64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (_: Throwable) {
        null
    }
}

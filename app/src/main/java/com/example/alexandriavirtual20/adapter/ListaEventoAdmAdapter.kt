package com.example.alexandriavirtual20.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.R
import com.example.alexandriavirtual20.model.Evento
import com.google.firebase.firestore.FirebaseFirestore

class ListaEventoAdmAdapter(
    private val eventos: MutableList<Evento>,
    private val btnEditarClick: (Evento) -> Unit
) : RecyclerView.Adapter<ListaEventoAdmAdapter.ViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagem: ImageView = view.findViewById(R.id.imagemEventoListaAdm)
        val nome: TextView = view.findViewById(R.id.nomeEventoListaAdm)
        val horario: TextView = view.findViewById(R.id.horarioEventoListaAdm)
        val btnEditar: ImageButton = view.findViewById(R.id.botaoEditarEventoListaAdm)
        val checkBox: CheckBox = view.findViewById(R.id.checkBoxEventoListaAdm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.u_cardview_eventos_adm, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evento = eventos[position]

        if (!evento.imagem.isNullOrEmpty()) {
            val imageBytes = Base64.decode(evento.imagem, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.imagem.setImageBitmap(bitmap)
        } else {
            holder.imagem.setImageResource(R.drawable.padraopng)
        }

        holder.nome.text = evento.nome
        holder.horario.text = evento.horario

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = evento.isSelected
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            evento.isSelected = isChecked
        }

        holder.btnEditar.setOnClickListener { btnEditarClick(evento) }
    }

    override fun getItemCount(): Int = eventos.size

    /**
     * Exclui os eventos marcados do Firestore e da lista local.
     */
    fun excluirSelecionados() {
        val selecionados = eventos.filter { it.isSelected }

        if (selecionados.isEmpty()) return

        val colecao = firestore.collection("evento")
        var deletados = 0
        val total = selecionados.size

        for (evento in selecionados) {
            // Aqui idealmente você usaria o id salvo no Evento, se tiver.
            colecao.whereEqualTo("nome", evento.nome).get()
                .addOnSuccessListener { query ->
                    for (doc in query) {
                        colecao.document(doc.id).delete()
                            .addOnSuccessListener {
                                deletados++
                                if (deletados == total) {
                                    // Depois de deletar todos do Firebase, atualiza a lista local
                                    eventos.removeAll { it.isSelected }
                                    notifyDataSetChanged()
                                }
                            }
                            .addOnFailureListener {
                                // Se der erro em algum delete, apenas loga
                                it.printStackTrace()
                            }
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }

    fun isEmpty(): Boolean {
        return eventos.none { it.isSelected }
    }
}

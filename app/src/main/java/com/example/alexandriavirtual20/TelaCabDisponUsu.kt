package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.CabineAdapter
import com.example.alexandriavirtual20.model.Cabine
import com.google.firebase.firestore.FirebaseFirestore

class TelaCabDisponUsu : AppCompatActivity() {
    private lateinit var fb : FirebaseFirestore
    private lateinit var adapter: CabineAdapter
    private lateinit var rv: RecyclerView
    private lateinit var btnVoltar: ImageButton
    private lateinit var etPeriodo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_cab_dispon_usu)

        rv = findViewById(R.id.rvCabines)
        btnVoltar = findViewById(R.id.imageButton4)
        etPeriodo = findViewById(R.id.tvPeriodo)

        fb = FirebaseFirestore.getInstance()

        adapter = CabineAdapter { cabine ->
            abrirTelaConfirmacao(cabine)
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val data = intent.getStringExtra("dataFormatada")
        val periodo = intent.getStringExtra("periodo")

        if (data != null && periodo != null) {
            etPeriodo.text = data + " - " + periodo
            carregarCabinesDoDiaEHorario(data, periodo)
        }

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun carregarCabinesDoDiaEHorario(dia: String, horario: String) {
        fb.collection("cabines")
            .get()
            .addOnSuccessListener { snap ->
                val todas = snap.documents.mapNotNull { it.toObject(Cabine::class.java) }

                val livres = todas.filter { cabine ->
                    val ocupadaNesseHorario =
                        cabine.dia == dia &&
                                cabine.horario == horario &&
                                cabine.livre == false
                    !ocupadaNesseHorario
                }

                adapter.submitList(livres)

                if (livres.isEmpty()) {
                    Toast.makeText(this, "Nenhuma cabine livre!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun abrirTelaConfirmacao(cabine: Cabine) {
        val data = intent.getStringExtra("dataFormatada")
        val periodo = intent.getStringExtra("periodo")

        val intent = Intent(this, TelaConfirmReservaUsu::class.java).apply {
            putExtra("CABINE_ID", cabine.id)
            putExtra("cabine", cabine.numero)
            putExtra("data", data)
            putExtra("periodo", periodo)
        }
        startActivity(intent)
    }
}
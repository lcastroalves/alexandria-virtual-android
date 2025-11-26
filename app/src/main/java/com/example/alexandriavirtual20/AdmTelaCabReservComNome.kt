package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.CabineAdmAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.example.alexandriavirtual20.model.Cabine

class AdmTelaCabReservComNome : AppCompatActivity() {
    private lateinit var fb : FirebaseFirestore
    private lateinit var adapter: CabineAdmAdapter
    private lateinit var rv: RecyclerView
    private lateinit var btnVoltar: ImageButton
    private lateinit var etData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_cab_reserv_com_nome)

        rv = findViewById(R.id.rvCabines)
        btnVoltar = findViewById(R.id.imageButton4)
        etData = findViewById(R.id.tvPeriodo)

        fb = FirebaseFirestore.getInstance()

        adapter = CabineAdmAdapter(::buscarFotoAlunoPorNome)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val data = intent.getStringExtra("dataFormatada")
        etData.text = data

        if (data != null) {
            carregarCabinesDoDia(data)
        }

        btnVoltar.setOnClickListener {
            val intent = Intent(this, AdmAMain::class.java)
            intent.putExtra("fragment_destino", "reserva")
            startActivity(intent)
        }
    }

    private fun buscarFotoAlunoPorNome(nome: String, callback: (String?) -> Unit) {
        fb.collection("usuario")
            .whereEqualTo("nome", nome)
            .limit(1)
            .get()
            .addOnSuccessListener { snap ->
                val foto = snap.documents.firstOrNull()?.getString("fotoPerfil")
                callback(foto)
            }
            .addOnFailureListener { callback(null) }
    }

    private fun carregarCabinesDoDia(dia: String) {
        fb.collection("reservasCabines")
            .whereEqualTo("dia", dia)
            .get()
            .addOnSuccessListener { snap ->
                val lista = snap.documents.mapNotNull { doc ->
                    doc.toObject(Cabine::class.java)
                }
                adapter.submitList(lista)
            }
    }
}
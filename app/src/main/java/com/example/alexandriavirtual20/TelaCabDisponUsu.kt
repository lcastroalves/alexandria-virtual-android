package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.CabineAdapter
import com.example.alexandriavirtual20.model.Cabine

class TelaCabDisponUsu : AppCompatActivity() {
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

        adapter = CabineAdapter()

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val data = intent.getStringExtra("dataFormatada")
        val periodo = intent.getStringExtra("periodo")

        etPeriodo.text = data + " - " + periodo

        val lista = listOf(
            Cabine(1, "Cabine 102", "Bloco B"),
            Cabine(2, "Cabine 95", "Bloco B"),
            Cabine(3, "Cabine 23", "Bloco A"),
        )
        adapter.submitList(lista)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.CabineAdapter
import com.example.alexandriavirtual20.adapter.CabineAdmAdapter
import com.example.alexandriavirtual20.model.Cabine
import com.example.alexandriavirtual20.model.CabineAdm

class AdmTelaCabReservHorEsp : AppCompatActivity() {
    private lateinit var adapter: CabineAdmAdapter
    private lateinit var rv: RecyclerView
    private lateinit var btnVoltar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_cab_reserv_hor_esp)

        rv = findViewById(R.id.rvCabines)
        btnVoltar = findViewById(R.id.imageButton4)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val listaAdm = listOf(
            CabineAdm(1, "Cabine 102", "Lara Castro"),
            CabineAdm(2, "Cabine 95", "Thiago Narak"),
        )
        adapter.submitList(listaAdm)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
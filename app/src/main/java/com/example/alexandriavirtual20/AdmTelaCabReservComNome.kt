package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.CabineAdmAdapter
import com.example.alexandriavirtual20.model.CabineAdm

class AdmTelaCabReservComNome : AppCompatActivity() {
    private lateinit var adapter: CabineAdmAdapter
    private lateinit var rv: RecyclerView
    private lateinit var btnVoltar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_cab_reserv_com_nome)

        rv = findViewById(R.id.rvCabines)
        btnVoltar = findViewById(R.id.imageButton4)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val listaAdm = listOf(
            CabineAdm(1, "Cabine 102", "Lara Castro"),
            CabineAdm(2, "Cabine 95", "Thiago Narak"),
            CabineAdm(3, "Cabine 184", "Dillan Medeiros")
        )
        adapter.submitList(listaAdm)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
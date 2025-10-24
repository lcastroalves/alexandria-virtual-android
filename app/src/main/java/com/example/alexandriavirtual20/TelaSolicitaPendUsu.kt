package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.SoliPendAdapter
import com.example.alexandriavirtual20.model.SoliPend

class TelaSolicitaPendUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_solicita_pend_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recySoliPend)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val soliPends = mutableListOf(
            SoliPend("Ciência da computação - ", "5 estrelas", "Dillan", R.drawable.livro1, true),
            SoliPend("Ciência da computação - ", "2 estrelas", "Lara",  R.drawable.livro2, false),
            SoliPend("Ciência da computação - ", "2 estrelas", "Nicolly",  R.drawable.livro4, true)
            )

        var adapter: SoliPendAdapter? = null

        // Cria o Adapter e envia a lista produtos para ele
        adapter = SoliPendAdapter(
            soliPends,
            onExcluirClick = { soliPend ->
                adapter?.removerSoliPend(soliPend)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
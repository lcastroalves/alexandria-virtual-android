package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.TornarAdmAdapter
import com.example.alexandriavirtual20.model.Usuario

class AdmTelaAdicionarAdm : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adm_tela_adicionar_adm)

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recyTornarAdm)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val usuarios = mutableListOf(
            Usuario(12349876, R.drawable.narak, "Narak Oliveira"),
            Usuario(12349876, R.drawable.narak, "Narak Oliveira"),
            Usuario(12349876, R.drawable.narak, "Narak Oliveira"),
            Usuario(12349876, R.drawable.narak, "Narak Oliveira")

            )

        var adapter: TornarAdmAdapter? = null

        // Cria o Adapter e envia a lista produtos para ele
        adapter = TornarAdmAdapter(
            usuarios,
            onClick = { usuario ->
                adapter?.tornarAdm(usuario)
                Toast.makeText(this, "Adicionado com sucesso", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
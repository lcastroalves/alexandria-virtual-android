package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.SoliPendAdapter

class TelaSolicitaPendUsu : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SoliPendAdapter

//    private val listaOriginal = mutableListOf<SoliPend>()
//    private val listaFiltrada = mutableListOf<SoliPend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_solicita_pend_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        recyclerView = findViewById(R.id.recySoliPend)
        searchView = findViewById(R.id.searchViewLivros)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // --------------------------------------
        // LISTA INICIAL DOS PEDIDOS (ORIGINAL)
        // --------------------------------------
//        listaOriginal.addAll(
//            listOf(
//                SoliPend("Ciência da computação", "e tecnologias digitais", "5 estrelas", "Ernane Rosa Martins", R.drawable.livro1, true),
//                SoliPend("Java como programar", "", "2 estrelas", "Paul J. Deitel", R.drawable.livro2, false),
//                SoliPend("Redes de computadores", "", "2 estrelas", "Tanenbaum e Wetherall", R.drawable.livro4, true)
//            )
//        )

//        // A filtrada começa igual à original
//        listaFiltrada.addAll(listaOriginal)
//
//        // --------------------------------------
//        // CONFIGURAÇÃO DO ADAPTER
//        // --------------------------------------
//        adapter = SoliPendAdapter(
//            listaFiltrada,
//            onExcluirClick = { soliPend ->
//                adapter.removerSoliPend(soliPend)
//                listaOriginal.remove(soliPend)
//            }
//        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        configurarPesquisa()
    }

    // --------------------------------------
    // 🔍 BARRA DE PESQUISA FUNCIONANDO
    // --------------------------------------
    private fun configurarPesquisa() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {

                val texto = newText?.trim()?.lowercase().orEmpty()

//                listaFiltrada.clear()
//
//                if (texto.isEmpty()) {
//                    listaFiltrada.addAll(listaOriginal)
//                } else {
//                    listaFiltrada.addAll(
//                        listaOriginal.filter { soli ->
//
//                            soli.nome.lowercase().contains(texto) ||
//                                    soli.autor.lowercase().contains(texto) ||
//                                    soli.avaliacao.lowercase().contains(texto)
//
//                        }
//                    )
//                }

                adapter.notifyDataSetChanged()
                return true
            }
        })
    }
}

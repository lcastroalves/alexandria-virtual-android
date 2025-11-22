package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroAdapterMeus
import com.example.alexandriavirtual20.model.Livro

class TelaMeusLivrosUsu : AppCompatActivity() {

    private val listaOriginal = mutableListOf<Livro>()
    private val listaFiltrada = mutableListOf<Livro>()
    private lateinit var adapter: LivroAdapterMeus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_meus_livros_usu)

        val btnBack: ImageButton = findViewById(R.id.btnback)
        val btnAvaliar: Button = findViewById(R.id.btnAvaliarLivros)
        val txtProgresso: TextView = findViewById(R.id.txtProgresso)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerLivros)

        val filtroAlugados: TextView = findViewById(R.id.filtroAlugados)
        val filtroLidos: TextView = findViewById(R.id.filtroLidos)
        val filtroAZ: TextView = findViewById(R.id.filtroAZ)

        btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // -----------------------------
        // 📚 LISTA DE EXEMPLO (substituir pelo Firebase depois)
        // -----------------------------
        listaOriginal.addAll(
            listOf(

                Livro("333333333", "Python - A Revolução", "Paul J. Deitel", "", "", "2020" ,200, true,"2018"),
                Livro("333333333", "Ciência da Computação", "Ername Rosa Martins", "", "", "2020" ,200, true,"2024"),
                Livro("333333333", "Java Como Programar", "Paul J. Deitel", "", "", "2020" ,200, true,"2020")
            )
        )

        listaFiltrada.addAll(listaOriginal)

        // -----------------------------
        // 🔄 CONFIGURA ADAPTER
        // -----------------------------
        adapter = LivroAdapterMeus(listaFiltrada) { livro ->
            val intent = Intent(this, TelaInfoLivroUsu::class.java)
            intent.putExtra("livroId", livro.id)
            startActivity(intent)
        }

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        txtProgresso.text = "${listaOriginal.size}/12"

        // -----------------------------
        // ⭐ APLICAR FILTROS
        // -----------------------------
        filtroAlugados.setOnClickListener {
            aplicarFiltro("alugados")
        }

        filtroLidos.setOnClickListener {
            aplicarFiltro("lidos")
        }

        filtroAZ.setOnClickListener {
            aplicarFiltro("az")
        }

        btnAvaliar.setOnClickListener {
            startActivity(Intent(this, TelaReviewUsu::class.java))
        }
    }

    // ------------------------------------------------
    // 🧠 FUNÇÃO CENTRAL DE FILTRAGEM/ORDENAÇÃO
    // ------------------------------------------------
    private fun aplicarFiltro(tipo: String) {
        listaFiltrada.clear()

        when (tipo) {

            "alugados" -> {
                listaFiltrada.addAll(listaOriginal.sortedByDescending {
                    it.anoLancamento.toIntOrNull() ?: 0
                })
            }

            "lidos" -> {
                listaFiltrada.addAll(listaOriginal.sortedBy {
                    it.anoLancamento.toIntOrNull() ?: 0
                })
            }

            "az" -> {
                listaFiltrada.addAll(listaOriginal.sortedBy { it.titulo.lowercase() })
            }
        }

        adapter.notifyDataSetChanged()
    }
}

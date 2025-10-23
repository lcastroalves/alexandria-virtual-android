package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.ProdutoAdapter
import com.example.alexandriavirtual20.model.Produto


class AdmTelaProdutos : AppCompatActivity() {

    private lateinit var btnVoltar : ImageButton
    private lateinit var btnAdProd : Button
    private lateinit var btnExcProd : ImageButton
    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adm_tela_produtos)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnAdProd = findViewById(R.id.btnAdProd)
        btnExcProd = findViewById(R.id.btnExcProd)
        recyclerView = findViewById(R.id.recyclerView2)


        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnAdProd.setOnClickListener {
            intent = Intent(this, AdmTelaCadasProd::class.java)
            startActivity(intent)
        }

        val produtos = mutableListOf(
            Produto("Ciência da computação", "Ernane Rosa Martins", R.drawable.livro1),
            Produto("Ciência da computação", "Ernane Rosa Martins", R.drawable.livro2),
            Produto("Ciência da computação", "Ernane Rosa Martins", R.drawable.livro3),
            Produto("Ciência da computação", "Ernane Rosa Martins", R.drawable.livro1),
            Produto("Ciência da computação", "Ernane Rosa Martins", R.drawable.livro1),
            Produto("Ciência da computação", "Ernane Rosa Martins", R.drawable.livro2),
            Produto("Ciência da computação", "Ernane Rosa Martins", R.drawable.livro3),
            Produto("Ciência da computação", "Ernane Rosa Martins", R.drawable.livro1),
        )

        // Cria o Adapter e envia a lista produtos para ele
        val adapter = ProdutoAdapter(produtos){ produto ->
            val intent = Intent(this, AdmTelaEditarProduto::class.java)
            startActivity(intent)

            // Somente coisas que dizem respeito ao item individual
        }

        btnExcProd.setOnClickListener {
            adapter.excluirSelecionados()
        }

        // Precisa definir a orientação (lá ele) do recycleview antes de chamar o adapter, se não ele fica tímido e não aparece

        /*
            ------ OPCAO HORIZONTAL -----
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

         */

        recyclerView.layoutManager = LinearLayoutManager(this)  // OPCAO VERTICAL
        recyclerView.adapter = adapter
    }
}

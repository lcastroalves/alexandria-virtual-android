package com.example.alexandriavirtual20

import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.ProdutoAdapter
import com.example.alexandriavirtual20.model.Produto
import com.google.firebase.firestore.FirebaseFirestore


class AdmTelaProdutos : AppCompatActivity() {

    private lateinit var btnVoltar : ImageButton
    private lateinit var btnAdProd : Button
    private lateinit var btnExcProd : ImageButton
    private lateinit var recyclerView : RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ProdutoAdapter


    private var listaProdutos = mutableListOf<Produto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adm_tela_produtos)

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnAdProd = findViewById(R.id.btnAdProd)
        btnExcProd = findViewById(R.id.btnExcProd)
        recyclerView = findViewById(R.id.recyTornarAdm)
        searchView = findViewById(R.id.searchView)

        firestore = FirebaseFirestore.getInstance()

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnAdProd.setOnClickListener {
            intent = Intent(this, AdmTelaCadasProd::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProdutoAdapter(listaProdutos){ produto ->
            val intent = Intent(this, AdmTelaEditarProduto::class.java)
            intent.putExtra("titulo", produto.titulo)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarProdutos(newText ?: "")
                return true
            }
        })

        btnExcProd.setOnClickListener {
            confirmarExclusao()
        }

        carregarProdutos()
    }

    private fun carregarProdutos() {

        firestore.collection("produtos")
            .addSnapshotListener { snaps, e ->
                if (e != null || snaps == null) return@addSnapshotListener

                listaProdutos.clear()
                for (doc in snaps.documents) {
                    val produto = Produto(
                        titulo = doc.getString("titulo") ?: "",
                        autor = doc.getString("autor") ?: "",
                        imageBase64 = doc.getString("imagem") ?: ""
                    )
                    listaProdutos.add(produto)
                }
                adapter.atualizarLista(listaProdutos)
            }

    }

    private fun filtrarProdutos(query: String) {
        val listaFiltrada = listaProdutos.filter {
            it.titulo.contains(query, ignoreCase = true) || it.autor.contains(query, ignoreCase = true)
        }
        adapter.atualizarLista(listaFiltrada.toMutableList())
    }

    private fun confirmarExclusao() {
        val selecionados = adapter.getSelecionados()
        if (selecionados.isEmpty()) {
            Toast.makeText(this, "Selecione um item.", Toast.LENGTH_SHORT).show()
            return
        }

        val mensagem = if (selecionados.size == 1) {
            "Tem certeza que deseja excluir o produto \"${selecionados[0].titulo}\"?"
        } else {
            "Tem certeza que deseja excluir ${selecionados.size} produtos?"
        }

        AlertDialog.Builder(this)
            .setTitle("Confirmação")
            .setMessage(mensagem)
            .setPositiveButton("Sim") { dialog, _ ->
                excluirDoFirestore(selecionados)
                dialog.dismiss()
            }
            .setNegativeButton("Não") { dialog, _ -> dialog.dismiss() }
            .show()

    }

    private fun excluirDoFirestore(selecionados: List<Produto>) {
        val colecao = firestore.collection("produtos")
        var deletados = 0
        val total = selecionados.size

        for (produto in selecionados) {
            colecao.whereEqualTo("titulo", produto.titulo).get().addOnSuccessListener { query ->
                    for (doc in query) {
                        colecao.document(doc.id).delete()
                            .addOnSuccessListener {
                                deletados++
                                if (deletados == total) {

                                    adapter.excluirSelecionados()
                                    carregarProdutos()
                                }
                            }
                    }
                }
        }
    }


}

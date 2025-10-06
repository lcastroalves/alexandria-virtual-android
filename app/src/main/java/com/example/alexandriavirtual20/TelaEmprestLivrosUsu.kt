package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import kotlin.jvm.java

class TelaEmprestLivrosUsu : AppCompatActivity() {
    lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_emprest_livros_usu)
        val spinnerAutor: Spinner = findViewById(R.id.spinnerAutor)
        val spinnerGenero: Spinner = findViewById(R.id.spinnerGenero)
        val spinnerMaisPopulares: Spinner = findViewById(R.id.spinnerMaisPopulares)
        val spinnerLancamento: Spinner = findViewById(R.id.spinnerLancamento)
        val titulo1: TextView = findViewById(R.id.txtTitulo1)
        val titulo2: TextView = findViewById(R.id.txtTitulo2)
        searchView = findViewById(R.id.searchViewLivros)

        val btnAvaliacoes1: TextView = findViewById(R.id.btnAvaliacoes1)
        val btnAvaliacoes2: TextView = findViewById(R.id.btnAvaliacoes2)
        val btnback: ImageButton = findViewById(R.id.btnback)

        val check1: CheckBox = findViewById(R.id.checkLivro1)
        val check2: CheckBox = findViewById(R.id.checkLivro2)

        val btnConfirmar: Button = findViewById(R.id.btnConfirmar)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Quando o usuário pressiona ENTER / Buscar
                if (!query.isNullOrEmpty()) {
                    Toast.makeText(this@TelaEmprestLivrosUsu, "Buscando: $query", Toast.LENGTH_SHORT).show()
                    // Aqui você pode filtrar sua lista de livros
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Quando o texto muda (digitação em tempo real)
                if (!newText.isNullOrEmpty()) {
                    // Filtra enquanto o usuário digita
                }
                return true
            }
        })


        ArrayAdapter.createFromResource(
            this,
            R.array.autores_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAutor.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.generos_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGenero.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.populares_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMaisPopulares.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.lancamentos_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerLancamento.adapter = adapter
        }
        titulo1.setOnClickListener {
            val intent = Intent(this, TelaInfoLivroUsu::class.java)
            startActivity(intent)
        }

        titulo2.setOnClickListener {
            val intent = Intent(this, TelaInfoLivroUsu::class.java)
            startActivity(intent)
        }

        // Clique em avaliações → abre tela de avaliações
        btnAvaliacoes1.setOnClickListener {
            val intent = Intent(this, TelaAvaliacoesUsu::class.java)
            startActivity(intent)
        }

        btnAvaliacoes2.setOnClickListener {
            val intent = Intent(this, TelaAvaliacoesUsu::class.java)
            startActivity(intent)
        }
        btnback.setOnClickListener {
            val intent = Intent(this, TelaMenuEmprestUsu::class.java)
            startActivity(intent)
        }



        // Botão confirmar → mostra os livros selecionados
        btnConfirmar.setOnClickListener {
            val selecionados = mutableListOf<String>()

            if (check1.isChecked) selecionados.add("Ciência da Computação e Tecnologias Digitais")
            if (check2.isChecked) selecionados.add("Java como Programar")

            Toast.makeText(this, "Selecionados: $selecionados", Toast.LENGTH_SHORT).show()
        }
    }
}


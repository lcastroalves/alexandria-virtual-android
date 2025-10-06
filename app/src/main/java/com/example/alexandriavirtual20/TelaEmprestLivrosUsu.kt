package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import kotlin.jvm.java

class TelaEmprestLivrosUsu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_emprest_livros_usu)
        val spinnerAutor: Spinner = findViewById(R.id.spinnerAutor)
        val spinnerGenero: Spinner = findViewById(R.id.spinnerGenero)
        val spinnerMaisPopulares: Spinner = findViewById(R.id.spinnerMaisPopulares)
        val spinnerLancamento: Spinner = findViewById(R.id.spinnerLancamento)
        val titulo1: TextView = findViewById(R.id.txtTitulo1)
        val titulo2: TextView = findViewById(R.id.txtTitulo2)

        val btnAvaliacoes1: TextView = findViewById(R.id.btnAvaliacoes1)
        val btnAvaliacoes2: TextView = findViewById(R.id.btnAvaliacoes2)

        val check1: CheckBox = findViewById(R.id.checkLivro1)
        val check2: CheckBox = findViewById(R.id.checkLivro2)

        val btnConfirmar: Button = findViewById(R.id.btnConfirmar)


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

        // Botão confirmar → mostra os livros selecionados
        btnConfirmar.setOnClickListener {
            val selecionados = mutableListOf<String>()

            if (check1.isChecked) selecionados.add("Ciência da Computação e Tecnologias Digitais")
            if (check2.isChecked) selecionados.add("Java como Programar")

            Toast.makeText(this, "Selecionados: $selecionados", Toast.LENGTH_SHORT).show()
        }
    }
}


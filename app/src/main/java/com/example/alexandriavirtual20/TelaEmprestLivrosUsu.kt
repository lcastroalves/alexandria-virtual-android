package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroAdapter
import com.example.alexandriavirtual20.model.Livro

class TelaEmprestLivrosUsu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_emprest_livros_usu)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerLivros)
        val btnConfirmar: Button = findViewById(R.id.btnConfirmar)
        val btnBack: ImageButton = findViewById(R.id.btnback)
        val spinnerAutor: Spinner = findViewById(R.id.spinnerAutor)
        val spinnerGenero: Spinner = findViewById(R.id.spinnerGenero)
        val spinnerMaisPopulares: Spinner = findViewById(R.id.spinnerMaisPopulares)
        val spinnerLancamento: Spinner = findViewById(R.id.spinnerLancamento)

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

        val livros = listOf(
            Livro(
                "44444444",
                "Ciência da Computação e Tecnologias Digitais",
                "Ername Rosa Martins",
                "Ername Rosa Martins",
                R.drawable.livro1,
                "130",
                true
            ),
            Livro(
                "33333333",
                "Java como Programar",
                "Paul J.Deitel",
                "Paul J.Deitel",
                R.drawable.livro2,
                "130",
                true
            ),
            Livro(
                "2222222",
                "Java como Programar",
                "Paul J.Deitel",
                "Paul J.Deitel",
                R.drawable.livro3,
                "130",
                false
            ),
            Livro(
                "11111111",
                "Redes de Computadores",
                "Tanenbaum & Wetherall",
                "Tanenbaum & Wetherall",
                R.drawable.livro4,
                "130",
                false

            )
        )

        val adapter = LivroAdapter(livros) { livro ->
            val intent = Intent(this, TelaAvaliacoesUsu::class.java)
            intent.putExtra("livro", livro)  // → envia o livro correto
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnConfirmar.setOnClickListener {
            val selecionados = adapter.getSelecionados()

            if (selecionados.isEmpty()) {
                Toast.makeText(this, "Nenhum livro selecionado!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, TelaRetirarLivroUsu::class.java)
            intent.putParcelableArrayListExtra("livrosSelecionados", ArrayList(selecionados))
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}

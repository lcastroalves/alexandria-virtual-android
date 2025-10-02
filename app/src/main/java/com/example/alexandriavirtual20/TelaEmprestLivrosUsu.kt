package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaEmprestLivrosUsu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_emprest_livros_usu)
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
    }
}
package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaEventosFuturUsu : AppCompatActivity() {
    private lateinit var btnVoltar: Button
    private lateinit var dataEvento: TextView
    private lateinit var tituloEvento: TextView
    private lateinit var descricaoEvento: TextView
    private lateinit var imagemEvento: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_eventos_futuros_usu)
        
        btnVoltar = findViewById(R.id.botaoVoltar)

    }
}
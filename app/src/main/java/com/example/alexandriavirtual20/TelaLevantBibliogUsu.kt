package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class TelaLevantBibliogUsu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_levant_bibliog_usu)

        val botaoVoltar = findViewById<ImageButton>(R.id.botaoVoltar)
        botaoVoltar.setOnClickListener{
            val intent = Intent(this, TelaInicioUsu::class.java)
            startActivity(intent)
            finish()
        }
    }
}
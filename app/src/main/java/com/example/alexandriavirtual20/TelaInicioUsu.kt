package com.example.alexandriavirtual20

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class TelaInicioUsu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_inicio_usu)

//        // ESSE CODIGO SERVE PARA A INFORMACOES DO LIVRO! -> Cuidado, estava ocasionando confusao na troca de telas
//        val intent = Intent(this, TelaInfoLivroUsu::class.java)
//        intent.putExtra("anterior", "TelaInicioUsu")
//        startActivity(intent)
    }
}
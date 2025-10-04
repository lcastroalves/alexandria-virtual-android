package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import kotlin.jvm.java

class TelaMenuEmprestUsu : AppCompatActivity() {
    lateinit var btnEmprestimo: LinearLayout
    lateinit var btnMeusLivros: LinearLayout

    lateinit var btnHistoricoLeit: LinearLayout

    lateinit var btnSolicPendentes: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_menu_emprest_usu)

        btnEmprestimo = findViewById(R.id.btnEmprestimo)
        btnMeusLivros = findViewById(R.id.btnMeusLivros)
        btnHistoricoLeit = findViewById(R.id.btnHistoricoLeit)
        btnSolicPendentes = findViewById(R.id.btnSolicPendentes)

        btnEmprestimo.setOnClickListener {
            trocarParaTelaEmprestimoDeLivros()
        }
        btnMeusLivros.setOnClickListener {
            trocarParaTelaMeusLivros()
        }
        btnHistoricoLeit.setOnClickListener {
            trocarParaTelaHistoricoLeitura()
        }
        btnSolicPendentes.setOnClickListener {
            trocarParaTelaSolicPendentes()
        }


    }
    fun trocarParaTelaEmprestimoDeLivros(){
        var intencao = Intent(this, TelaEmprestLivrosUsu::class.java)
        startActivity(intencao)
    }
    fun trocarParaTelaMeusLivros(){
        var intencao = Intent(this, TelaMeusLivrosUsu::class.java)
        startActivity(intencao)
    }
    fun trocarParaTelaHistoricoLeitura(){
        var intencao = Intent(this, TelaHistoricoUsu::class.java)
        startActivity(intencao)
    }
    fun trocarParaTelaSolicPendentes(){
        var intencao = Intent(this, TelaSolicitaPendUsu::class.java)
        startActivity(intencao)
    }
}



package com.example.alexandriavirtual20

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AMain : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val destino = intent.getStringExtra("fragment_destino")

        if (savedInstanceState == null) {
            when (destino) {
                "reserva" -> {
                    bottomNavigationView.selectedItemId = R.id.reserva
                    replaceFragment(TelaReservaCabUsu())
                }
                else -> {
                    bottomNavigationView.selectedItemId = R.id.inicio
                    replaceFragment(TelaInicioUsu())
                }
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inicio -> {
                    replaceFragment(TelaInicioUsu())
                    true
                }
                R.id.emprestimo -> {
                    replaceFragment(TelaMenuEmprestUsu())
                    true
                }
                R.id.chatbot -> {
                    replaceFragment(TelaChatbotUsu())
                    true
                }
                R.id.reserva -> {
                    replaceFragment(TelaReservaCabUsu())
                    true
                }
                R.id.perfil -> {
                    replaceFragment(TelaPerfilUsu())
                    true
                }
                else -> false
            }
        }
    }
}

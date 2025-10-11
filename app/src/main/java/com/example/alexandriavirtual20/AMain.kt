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
        replaceFragment(TelaInicioUsu())

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inicio -> {
                    replaceFragment(TelaInicioUsu())
                    true
                }
                R.id.emprestimo -> {
                    //replaceFragment(TelaEmprestimoUsu())
                    true
                }
                R.id.chatbot -> {
                    //replaceFragment(TelaChatbotUsu())
                    true
                }
                R.id.reserva -> {
                    //replaceFragment(TelaReservaUsu())
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

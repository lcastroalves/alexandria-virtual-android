package com.example.alexandriavirtual20

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdmAMain : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    public fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_a_main)
        replaceFragment(AdmTelaInicio())

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

//        val destino = intent.getStringExtra("fragment_destino")
//
//        if (savedInstanceState == null) {
//            when (destino) {
//                "reserva" -> {
//                    bottomNavigationView.selectedItemId = R.id.reserva
//                    replaceFragment(TelaReservaCabUsu())
//                }
//                else -> {
//                    bottomNavigationView.selectedItemId = R.id.inicio
//                    replaceFragment(TelaInicioUsu())
//                }
//            }
//        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.inicio -> {
                    replaceFragment(AdmTelaInicio())
                    true
                }
                R.id.emprestimo -> {
                    replaceFragment(AdmTelaSolicPend())
                    true
                }
                R.id.chatbot -> {
                    replaceFragment(TelaChatbotUsu())
                    true
                }
                R.id.reserva -> {
                    replaceFragment(AdmTelaCabReserv())
                    true
                }
                R.id.perfil -> {
                    replaceFragment(AdmTelaPerfil())
                    true
                }
                else -> false
            }
        }
    }
}
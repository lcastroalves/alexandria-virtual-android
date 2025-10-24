package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class TelaConfirmReservaUsu : AppCompatActivity() {
    private lateinit var btnConfirmar: Button
    private lateinit var btnVoltar: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_confirm_reserva_usu)

        btnVoltar = findViewById(R.id.botaoVoltar7)
        btnConfirmar = findViewById(R.id.Confirmar)

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnConfirmar.setOnClickListener {
//            replaceFragment(TelaInicioUsu())
        }
    }

//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.main, fragment)
//        fragmentTransaction.commit()
//    }
}
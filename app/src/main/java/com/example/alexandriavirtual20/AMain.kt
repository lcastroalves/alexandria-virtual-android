package com.example.alexandriavirtual20

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.alexandriavirtual20.databinding.AMainBinding
import com.google.android.material.navigation.NavigationBarView

class AMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFragment(TelaInicioUsu())

        bottomNavigationView.setOnItemSelectedListener( { menuItem: MenuItem? ->
            val id = menuItem!!.getItemId()
            if (id == R.id.inicio) {
                replaceFragment(TelaInicioUsu())
            } else if (id == R.id.emprestimo) {
            } else if (id == R.id.chatbot) {
            } else if (id == R.id.reserva) {
            } else if (id == R.id.perfil) {
                replaceFragment(TelaPerfilUsu())
            }
            true
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = getSupportFragmentManager()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}

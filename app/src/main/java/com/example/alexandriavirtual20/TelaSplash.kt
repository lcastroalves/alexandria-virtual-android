package com.example.alexandriavirtual20

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.content.Intent
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaSplash : AppCompatActivity() {

    private lateinit var fbAuth: FirebaseAuth
    private lateinit var fireBase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_splash)

        fbAuth = FirebaseAuth.getInstance()
        fireBase = FirebaseFirestore.getInstance()

        val usuarioLogado = fbAuth.currentUser

        if(usuarioLogado != null){
            fireBase.collection("usuario").document(usuarioLogado.uid).get().addOnSuccessListener { doc ->

                val isAdmin = doc.getBoolean("admin") ?: false

                if(isAdmin){
                    val intent = Intent(this, AdmAMain::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, AMain::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            val intent = Intent(this, TelaLogin::class.java)
            startActivity(intent)
            finish()
        }

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent (this, TelaLogin::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}
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

        Handler(Looper.getMainLooper()).postDelayed({
            val usuarioLogado = fbAuth.currentUser

            if (usuarioLogado != null) {
                fireBase.collection("usuario").document(usuarioLogado.uid).get().addOnSuccessListener { doc ->
                        val isAdmin = doc.getBoolean("admin") ?: false

                        if (isAdmin) {
                            startActivity(Intent(this, AdmAMain::class.java))
                            finish()
                        }else{
                            startActivity(Intent(this,AMain::class.java))
                            finish()
                        }
                }
            } else {
                startActivity(Intent(this, TelaLogin::class.java))
                finish()
            }

        }, 3000)


        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }
}
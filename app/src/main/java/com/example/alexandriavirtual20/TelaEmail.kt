package com.example.alexandriavirtual20

import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaEmail : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var edtEmail: EditText
    private lateinit var btnEnviar: Button
    private lateinit var fbAuth : FirebaseAuth
    private lateinit var fireBase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_email)

        btnVoltar = findViewById(R.id.botaoVoltar)
        edtEmail = findViewById(R.id.editarEmail)
        btnEnviar = findViewById(R.id.botaoEnviar)

        fireBase = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnEnviar.setOnClickListener {

            val email = edtEmail.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Insira um e-mail válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fireBase.collection("usuario")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { docs ->
                    if (!docs.isEmpty) {
                        fbAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "E-mail de redefinição enviado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { erro ->
                                Toast.makeText(
                                    this,
                                    "Falha ao enviar e-mail",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    } else {
                        Toast.makeText(this, "E-mail não encontrado", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
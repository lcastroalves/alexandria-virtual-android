package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.jvm.java

class TelaLogin : AppCompatActivity() {

    private lateinit var edtEmail: TextView
    private lateinit var edtSenha: TextView
    private lateinit var txtEsqueceuSenha: TextView
    private lateinit var btnEntrar: Button
    private lateinit var txtCadastrar: TextView

    private lateinit var fireBase: FirebaseFirestore
    private lateinit var fbAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_login)

        edtEmail = findViewById(R.id.emailLogin)
        edtSenha = findViewById(R.id.senha)
        txtEsqueceuSenha = findViewById(R.id.esqueciSenha)
        btnEntrar = findViewById(R.id.botaoEntrar)
        txtCadastrar = findViewById(R.id.txtCadastrar)

        fireBase = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

        btnEntrar.setOnClickListener {
            val email = edtEmail.text.toString()
            val senha = edtSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            fbAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener{ task ->
                if(task.isSuccessful){

                    val userID = fbAuth.currentUser?.uid ?: return@addOnCompleteListener

                    fireBase.collection("usuario").document(userID).get().addOnSuccessListener { document ->

                        if (!document.exists()) {
                            Toast.makeText(this, "Usuário não encontrado!", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }

                        val isAdmin = document.getBoolean("admin")  ?: false


                        if(isAdmin){
                            val intent = Intent(this, AdmAMain::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, AMain::class.java)
                            startActivity(intent)
                        }

                    }
                } else {
                    Toast.makeText(this, "Credenciais incorretas, tente novamente!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        txtEsqueceuSenha.setOnClickListener {
            var intent = Intent(this, TelaEmail::class.java )
            startActivity(intent)
        }

        txtCadastrar.setOnClickListener {
            val intent = Intent(this, TelaCadastro::class.java)
            startActivity(intent)
        }
    }
}




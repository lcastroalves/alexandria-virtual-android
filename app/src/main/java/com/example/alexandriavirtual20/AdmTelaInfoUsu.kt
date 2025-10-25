package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AdmTelaInfoUsu : AppCompatActivity() {
    private lateinit var btnVoltar : ImageButton
    private lateinit var btnEditImg: ImageButton
    private lateinit var imgPerfil: ImageView
    private lateinit var abrirGaleria : ActivityResultLauncher<String>
    private lateinit var editNome: TextInputEditText
    private lateinit var editUsuario: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editSenha: TextInputEditText
    private lateinit var btnSalvar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_info_usu)

        btnVoltar = findViewById(R.id.botaoVoltar4)
        btnEditImg = findViewById(R.id.btnEdit2)
        imgPerfil = findViewById(R.id.logo)
        editNome = findViewById(R.id.etNome)
        editUsuario = findViewById(R.id.etUsuario)
        editEmail = findViewById(R.id.etEmail)
        editSenha = findViewById(R.id.etSenha)
        btnSalvar = findViewById(R.id.btnSalvar)

        abrirGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){uri ->
            if (uri != null){
                imgPerfil.setImageURI(uri)
            }
        }

        btnEditImg.setOnClickListener {
            abrirGaleria.launch("image/*")
        }

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSalvar.setOnClickListener {
            val nome    = editNome.text?.toString().orEmpty()
            val usuario = editUsuario.text?.toString().orEmpty()
            val email   = editEmail.text?.toString().orEmpty()
            val senha   = editSenha.text?.toString().orEmpty()

            Toast.makeText(this, "Alteração salva", Toast.LENGTH_SHORT).show()
        }
    }
}
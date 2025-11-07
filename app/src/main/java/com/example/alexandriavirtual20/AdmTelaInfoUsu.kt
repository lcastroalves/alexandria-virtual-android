package com.example.alexandriavirtual20

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class AdmTelaInfoUsu : AppCompatActivity() {
    private lateinit var fb : FirebaseFirestore
    private lateinit var btnVoltar : ImageButton
    private lateinit var btnEditImg: ImageButton
    private lateinit var imgPerfil: ImageView
    private lateinit var abrirGaleria : ActivityResultLauncher<String>
    private lateinit var editNome: TextInputEditText
    private lateinit var editUsuario: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editSenha: TextInputEditText
    private lateinit var btnSalvar: Button
    private var userId: String = " "
    private var novaFotoUri: Uri? = null


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
        fb = FirebaseFirestore.getInstance()

        userId = intent.getStringExtra("documentId") ?: run {
            Toast.makeText(this, "Usuário inválido.", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        abrirGaleria = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){uri ->
            if (uri != null){
                imgPerfil.setImageURI(uri)
                novaFotoUri = uri

                btnSalvar.isEnabled = true
                btnSalvar.alpha = 1f
            }
        }

        btnSalvar.isEnabled = false
        btnSalvar.alpha = 0.5f

        confBotoes()
        preencherCampos(userId)
        travarCampos()

    }

    private fun preencherCampos(userId: String){
        fb.collection("usuario").document(userId).get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val nome     = doc.getString("nome") ?: ""
                val usuario  = doc.getString("usuario") ?: ""
                val email    = doc.getString("email") ?: ""

                editNome.setText(nome)
                editUsuario.setText(usuario)
                editEmail.setText(email)
                editSenha.setText("*********")

                doc.getString("imgPerfil")?.let { b64 ->
                    if (b64.isNotEmpty()) {
                        val bytes = Base64.decode(b64, Base64.DEFAULT)
                        val bmp = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imgPerfil.setImageBitmap(bmp)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
            }
    }

    private fun travarCampos(){
        listOf(editNome, editUsuario, editEmail, editSenha).forEach { edit ->
            edit.isEnabled = false
        }
    }

    private fun confBotoes(){
        btnEditImg.setOnClickListener {
            abrirGaleria.launch("image/*")
        }

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSalvar.setOnClickListener {
            val imagemEnviada = (imgPerfil.drawable as? BitmapDrawable)?.bitmap
            if (imagemEnviada != null) {
                val outputStream = ByteArrayOutputStream()
                imagemEnviada.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val imagemBase64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)

                btnSalvar.isEnabled = false
                btnSalvar.alpha = 0.5f

                fb.collection("usuario").document(userId)
                    .update("imgPerfil", imagemBase64)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Alteração salva com sucesso!", Toast.LENGTH_SHORT).show()

                        btnSalvar.isEnabled = false
                        btnSalvar.alpha = 0.5f
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao salvar a nova imagem.", Toast.LENGTH_SHORT).show()
                        btnSalvar.isEnabled = true
                        btnSalvar.alpha = 1f
                    }

            } else {
                Toast.makeText(this, "Selecione uma imagem antes de salvar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
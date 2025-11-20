package com.example.alexandriavirtual20

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class AdmTelaEditarProduto : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnMudarImg: ImageButton
    private lateinit var capaLivro: ImageView
    private lateinit var edtTitulo : EditText
    private lateinit var edtAutor : EditText
    private lateinit var edtSubtitulo : EditText
    private lateinit var edtAnoPub : EditText
    private lateinit var edtCateg : EditText
    private lateinit var edtEdicao : EditText
    private lateinit var edtIdioma : EditText
    private lateinit var edtSinopse : EditText
    private lateinit var edtUnidade : EditText

    private lateinit var abrirGaleria: ActivityResultLauncher<String>
    private lateinit var btnSalvar: Button
    private lateinit var firestore: FirebaseFirestore
    private var imagemBase64: String = "" // Para guardar a imagem atual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adm_tela_editar_produto)

        val produtoTitulo = intent.getStringExtra("titulo") ?: return

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnMudarImg = findViewById(R.id.btnMudarImg)
        capaLivro = findViewById(R.id.capaEvento)
        btnSalvar = findViewById(R.id.btnSalvar)
        firestore = FirebaseFirestore.getInstance()

        edtTitulo = findViewById(R.id.titulo)
        edtAutor = findViewById(R.id.autor)
        edtSubtitulo = findViewById(R.id.subtitulo)
        edtAnoPub = findViewById(R.id.anoPub)
        edtCateg = findViewById(R.id.categoria)
        edtEdicao = findViewById(R.id.edicao)
        edtIdioma = findViewById(R.id.idioma)
        edtSinopse = findViewById(R.id.sinopse)
        edtUnidade = findViewById(R.id.unidade)

        // Buscar dados do produto pelo título
        firestore.collection("livros")
            .whereEqualTo("titulo", produtoTitulo)
            .get()
            .addOnSuccessListener { snapshots ->
                if (!snapshots.isEmpty) {
                    val doc = snapshots.documents[0]
                    edtTitulo.setText(doc.getString("titulo") ?: "")
                    edtAutor.setText(doc.getString("autor") ?: "")
                    edtSubtitulo.setText(doc.getString("subtitulo") ?: "")
                    edtAnoPub.setText(doc.getString("anoLancamento") ?: "")
                    edtCateg.setText(doc.getString("genero") ?: "")
                    edtEdicao.setText(doc.getString("edicao") ?: "")
                    edtIdioma.setText(doc.getString("idioma") ?: "")
                    edtSinopse.setText(doc.getString("sinopse") ?: "")
                    edtUnidade.setText(doc.getString("unidade") ?: "")
                    imagemBase64 = doc.getString("capa") ?: ""
                    if (imagemBase64.isNotEmpty()) {
                        val bytes = Base64.decode(imagemBase64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        capaLivro.setImageBitmap(bitmap)
                    }
                }
            }

        // Seleção de nova imagem
        abrirGaleria = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                capaLivro.setImageURI(uri)
                imagemBase64 = uriToBase64(uri)
            }
        }

        btnMudarImg.setOnClickListener { abrirGaleria.launch("image/*") }
        btnVoltar.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        btnSalvar.setOnClickListener {
            val tituloEdit = edtTitulo.text.toString()
            val autorEdit = edtAutor.text.toString()
            val subtituloEdit = edtSubtitulo.text.toString()
            val anoPubEdit = edtAnoPub.text.toString()
            val categEdit = edtCateg.text.toString()
            val edicaoEdit = edtEdicao.text.toString()
            val idiomaEdit = edtIdioma.text.toString()
            val sinopseEdit = edtSinopse.text.toString()
            val unidadeEdit = edtUnidade.text.toString()

            firestore.collection("livros")
                .whereEqualTo("titulo", produtoTitulo)
                .get()
                .addOnSuccessListener { snapshots ->
                    for (doc in snapshots.documents) {
                        firestore.collection("livros").document(doc.id).update(
                                mapOf(
                                    "titulo" to tituloEdit,
                                    "autor" to autorEdit,
                                    "subtitulo" to subtituloEdit,
                                    "anoLancamento" to anoPubEdit,
                                    "genero" to categEdit,
                                    "edicao" to edicaoEdit,
                                    "idioma" to idiomaEdit,
                                    "sinopse" to sinopseEdit,
                                    "unidade" to unidadeEdit,
                                    "capa" to imagemBase64
                                )
                            )
                    }
                    Toast.makeText(this, "Alterações salvas", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun uriToBase64(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}

package com.example.alexandriavirtual20

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
import android.graphics.Bitmap


class AdmTelaEditEvent : AppCompatActivity() {

    private lateinit var btnVoltar: ImageButton
    private lateinit var btnMudarImg: ImageButton
    private lateinit var imagemEvento: ImageView
    private lateinit var edtNome: EditText
    private lateinit var edtData: EditText
    private lateinit var edtHorario: EditText
    private lateinit var edtLocal: EditText
    private lateinit var edtBreveDescricao: EditText
    private lateinit var edtDescricao: EditText
    private lateinit var btnSalvar: Button

    private lateinit var abrirGaleria: ActivityResultLauncher<String>
    private lateinit var firestore: FirebaseFirestore
    private var imagemBase64: String = "" // Para armazenar a imagem atual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adm_tela_edit_event)

        val nomeEvento = intent.getStringExtra("nomeEvento") ?: return

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnMudarImg = findViewById(R.id.editImagemEvento)
        imagemEvento = findViewById(R.id.imagemEventoEdit)
        btnSalvar = findViewById(R.id.botaoSalvarEditarEvento)
        firestore = FirebaseFirestore.getInstance()

        edtNome = findViewById(R.id.tituloEditarEvento)
        edtData = findViewById(R.id.dataEditarEvento)
        edtHorario = findViewById(R.id.horarioEditarEvento)
        edtLocal = findViewById(R.id.localEditarEvento)
        edtBreveDescricao = findViewById(R.id.descricaoBreveEditarEvento)
        edtDescricao = findViewById(R.id.descricaoEditarEvento)

        // Buscar dados do evento pelo nome
        firestore.collection("evento")
            .whereEqualTo("nome", nomeEvento)
            .get()
            .addOnSuccessListener { snapshots ->
                if (!snapshots.isEmpty) {
                    val doc = snapshots.documents[0]
                    edtNome.setText(doc.getString("nome") ?: "")
                    edtData.setText(doc.getString("data") ?: "")
                    edtHorario.setText(doc.getString("horario") ?: "")
                    edtLocal.setText(doc.getString("local") ?: "")
                    edtBreveDescricao.setText(doc.getString("breveDescricao") ?: "")
                    edtDescricao.setText(doc.getString("descricao") ?: "")
                    imagemBase64 = doc.getString("imagem") ?: ""

                    if (imagemBase64.isNotEmpty()) {
                        val bytes = Base64.decode(imagemBase64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imagemEvento.setImageBitmap(bitmap)
                    }
                }
            }

        // Configurar seleção de imagem da galeria
        abrirGaleria = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imagemEvento.setImageURI(uri)
                imagemBase64 = uriToBase64(uri)
            }
        }

        btnMudarImg.setOnClickListener { abrirGaleria.launch("image/*") }
        btnVoltar.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        btnSalvar.setOnClickListener {
            val nomeEdit = edtNome.text.toString()
            val dataEdit = edtData.text.toString()
            val horarioEdit = edtHorario.text.toString()
            val localEdit = edtLocal.text.toString()
            val breveEdit = edtBreveDescricao.text.toString()
            val descricaoEdit = edtDescricao.text.toString()

            firestore.collection("evento")
                .whereEqualTo("nome", nomeEvento)
                .get()
                .addOnSuccessListener { snapshots ->
                    for (doc in snapshots.documents) {
                        firestore.collection("evento").document(doc.id).update(
                            mapOf(
                                "nome" to nomeEdit,
                                "data" to dataEdit,
                                "horario" to horarioEdit,
                                "local" to localEdit,
                                "breveDescricao" to breveEdit,
                                "descricao" to descricaoEdit,
                                "imagem" to imagemBase64
                            )
                        )
                    }
                    Toast.makeText(this, "Alterações salvas com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao salvar alterações.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun uriToBase64(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        val bytes = outputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}

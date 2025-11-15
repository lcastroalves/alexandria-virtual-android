package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import android.graphics.BitmapFactory
import android.util.Base64

class TelaInfoEventoUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnNotificar: Button
    private lateinit var imagem: ImageView
    private lateinit var nome: TextView
    private lateinit var horario: TextView
    private lateinit var descricao: TextView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var imagemBase64: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_info_evento_usu)

        val nomeEvento = intent.getStringExtra("nomeEvento") ?: return

        btnVoltar = findViewById(R.id.botaoVoltar)
        btnNotificar = findViewById(R.id.botaoNotificar)

        imagem = findViewById(R.id.imagemInfoAtividade)
        nome = findViewById(R.id.nomeInfoAtividade)
        horario = findViewById(R.id.horarioInfoAtividade)
        descricao = findViewById(R.id.descricaoInfoAtividade)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        firestore.collection("evento")
            .whereEqualTo("nome", nomeEvento)
            .get()
            .addOnSuccessListener { snapshots ->
                if (!snapshots.isEmpty) {
                    val doc = snapshots.documents[0]
                    nome.setText(doc.getString("nome") ?: "")
                    horario.setText(doc.getString("horario") ?: "")
                    descricao.setText(doc.getString("descricao") ?: "")
                    imagemBase64 = doc.getString("imagem") ?: ""

                    if (imagemBase64.isNotEmpty()) {
                        val bytes = Base64.decode(imagemBase64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imagem.setImageBitmap(bitmap)
                    }
                }
            }

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnNotificar.setOnClickListener {
//            val uid = auth.currentUser?.uid
//
//            if (uid == null) {
//                Toast.makeText(this, "Erro: usuário não logado.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val notificacao = hashMapOf(
//                "tipo" to "evento",
//                "referenciaId" to eventoId,
//                "nome" to eventoNome,
//                "data" to System.currentTimeMillis(),                // data atual
//                "prazo" to null,                                     // evento não tem prazo
//                "imagem" to eventoImagem,
//                "titulo" to "Lembrete de evento!",
//                "mensagem" to "O evento \"$eventoNome\" acontecerá em breve.",
//                "timestamp" to System.currentTimeMillis(),
//                "lida" to false
//            )
//
//            firestore.collection("usuario")
//                .document(uid)
//                .collection("notificacoes")
//                .add(notificacao)
//                .addOnSuccessListener {
//                    Toast.makeText(this, "Você será notificado!", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Erro ao criar notificação.", Toast.LENGTH_SHORT).show()
//                }
//        }
        }
    }
}
package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.LivroRetiradaAdapter
import com.example.alexandriavirtual20.model.Livro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaRetirarLivroUsu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_retirar_livro_usu)

        val btnBack: ImageButton = findViewById(R.id.btnback)
        val btnConfirmar: Button = findViewById(R.id.btnConfirmar)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerLivrosSelecionados)
        val txtStatusLivro: TextView = findViewById(R.id.textView7)

        val livrosSelecionados = intent.getParcelableArrayListExtra<Livro>("livrosSelecionados")
        var livroParaExibir: Livro? = null

        if (livrosSelecionados.isNullOrEmpty()) {

            txtStatusLivro.text = "Nenhum livro selecionado."
            Toast.makeText(this, "Nenhum livro recebido.", Toast.LENGTH_SHORT).show()

        } else {

            livroParaExibir = livrosSelecionados.first()

            txtStatusLivro.text =
                "Solicitação em andamento para ${livroParaExibir.titulo}"


            val nomes = livrosSelecionados.joinToString(", ") { it.titulo }
            Toast.makeText(this, "Livros recebidos: $nomes", Toast.LENGTH_LONG).show()

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = LivroRetiradaAdapter(livrosSelecionados)
        }


        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        btnConfirmar.setOnClickListener {

            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId == null) {
                Toast.makeText(this, "Erro: usuário não autenticado.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val livrosSelecionados = intent.getParcelableArrayListExtra<Livro>("livrosSelecionados")

            if (livrosSelecionados.isNullOrEmpty()) {
                Toast.makeText(this, "Nenhum livro para registrar.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = FirebaseFirestore.getInstance()

            livrosSelecionados.forEach { livro ->

                val emprestimo = hashMapOf(
                    "idLivro" to livro.id,
                    "idUsuario" to userId,
                    "prazo" to 0,
                    "situacao" to "pendente",
                    "dataSolicitacao" to com.google.firebase.Timestamp.now()
                )

                db.collection("emprestimo")
                    .add(emprestimo)
                    .addOnSuccessListener {
                        // Se quiser dar feedback, mas não é obrigatório
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Erro ao salvar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

            Toast.makeText(this, "Solicitação enviada com sucesso!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, TelaSolicitaPendUsu::class.java)
            startActivity(intent)
            finish()
        }

    }
}

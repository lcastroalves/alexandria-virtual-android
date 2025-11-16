package com.example.alexandriavirtual20

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class TelaInfoAtividadeUsu : AppCompatActivity() {
    private lateinit var btnVoltar: ImageButton
    private lateinit var imagem: ImageView
    private lateinit var nome: TextView
    private lateinit var horario: TextView
    private lateinit var descricao: TextView
    private lateinit var local: TextView
    private lateinit var fb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tela_info_atividade_usu)

        btnVoltar = findViewById(R.id.botaoVoltar)
        fb = FirebaseFirestore.getInstance()


        val nomeAtividade = intent.getStringExtra("nomeAtividade") ?: return
        val imagemAtividade = intent.getIntExtra("imagemAtividade", 0) ?: return
        val horarioAtividade = intent.getStringExtra("horarioAtividade") ?: return
        val descricaoAtividade = intent.getStringExtra("descricaoAtividade") ?: return
        val localAtividade = intent.getStringExtra("localAtividade") ?: return

        nome = findViewById(R.id.nomeInfoAtividade)
        horario = findViewById(R.id.horarioInfoAtividade)
        descricao = findViewById(R.id.descricaoInfoAtividade)
        local = findViewById(R.id.localInfoAtividade)
        imagem = findViewById(R.id.imagemInfoAtividade)


        nome.text = nomeAtividade
        horario.text = horarioAtividade
        descricao.text = "Descrição: " + descricaoAtividade
        local.text = "Local: " + localAtividade
        imagem.setImageResource((imagemAtividade).toInt())

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
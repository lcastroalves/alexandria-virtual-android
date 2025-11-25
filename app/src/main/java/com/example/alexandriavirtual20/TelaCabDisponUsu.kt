package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.CabineAdapter
import com.example.alexandriavirtual20.model.Cabine
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaCabDisponUsu : AppCompatActivity() {
    private lateinit var fb: FirebaseFirestore
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var adapter: CabineAdapter
    private lateinit var rv: RecyclerView
    private lateinit var btnVoltar: ImageButton
    private lateinit var etPeriodo: TextView

    private val TODAS_CABINES = (1..25).map { it.toString() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_cab_dispon_usu)

        rv = findViewById(R.id.rvCabines)
        btnVoltar = findViewById(R.id.imageButton4)
        etPeriodo = findViewById(R.id.tvPeriodo)

        fb = FirebaseFirestore.getInstance()
        fbAuth = FirebaseAuth.getInstance()

        adapter = CabineAdapter { cabine ->
            reservarCabineEIrParaConfirmacao(cabine)
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val data = intent.getStringExtra("dataFormatada")
        val inicio = intent.getStringExtra("inicio")
        val fim = intent.getStringExtra("fim")
        val periodo = intent.getStringExtra("periodo")

        if (data != null && periodo != null && inicio != null && fim != null) {
            etPeriodo.text = data + " | " + periodo
            carregarCabinesLivres(data, inicio, fim)
        } else {
            Toast.makeText(this, "Erro ao carregar horário selecionado.", Toast.LENGTH_SHORT).show()
        }

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    private fun carregarCabinesLivres(dia: String, inicioSel: String, fimSel: String) {
        fb.collection("reservasCabines")
            .whereEqualTo("dia", dia)
            .get()
            .addOnSuccessListener { snap ->
                val reservasDia = snap.toObjects(Cabine::class.java)

                val cabinesOcupadas = reservasDia
                    .filter { reserva ->
                        temConflito(inicioSel, fimSel, reserva.inicio, reserva.fim)
                    }
                    .map { it.numero }
                    .toSet()

                val cabinesLivres = TODAS_CABINES
                    .filter { it !in cabinesOcupadas }
                    .map { numeroCabine ->
                        Cabine(
                            numero = numeroCabine,
                            dia = dia,
                            inicio = inicioSel,
                            fim = fimSel
                        )
                    }

                adapter.submitList(cabinesLivres)

                if (cabinesLivres.isEmpty()) {
                    Toast.makeText(this, "Nenhuma cabine livre!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar cabines.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun reservarCabineEIrParaConfirmacao(cabine: Cabine) {
        val data = cabine.dia
        val inicio = cabine.inicio
        val fim = cabine.fim
        val periodo = "$inicio - $fim"

        val usuarioAtual = fbAuth.currentUser
        val uid = usuarioAtual?.uid ?: run {
            Toast.makeText(this, "Usuário não encontrado. Faça login novamente.", Toast.LENGTH_SHORT).show()
            return
        }

        fb.collection("usuario")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                if (!doc.exists()) {
                    Toast.makeText(this, "Dados do usuário não encontrados.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val nomeAluno = doc.getString("nome") ?: ""


        val reserva = hashMapOf(
            "numero" to cabine.numero,
            "dia" to data,
            "inicio" to inicio,
            "fim" to fim,
            "aluno" to nomeAluno
        )

        fb.collection("reservasCabines")
            .add(reserva)
            .addOnSuccessListener {
                val intent = Intent(this, TelaConfirmReservaUsu::class.java).apply {
                    putExtra("cabine", cabine.numero)
                    putExtra("data", data)
                    putExtra("periodo", periodo)
                }
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao salvar reserva.", Toast.LENGTH_SHORT).show()
            }
                }
    }
    private fun temConflito(
        inicioSel: String,
        fimSel: String,
        inicioCab: String,
        fimCab: String
    ): Boolean {
        return (inicioSel < fimCab) && (fimSel > inicioCab)
    }
}
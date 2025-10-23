package com.example.alexandriavirtual20

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alexandriavirtual20.adapter.UsuarioAdapter
import com.example.alexandriavirtual20.model.Usuario
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdmTelaUsuCadast : AppCompatActivity() {
    private lateinit var adapter: UsuarioAdapter
    private lateinit var rv: RecyclerView
    private lateinit var btnVoltar: ImageButton
    private lateinit var btnLixeira: ImageButton
    private lateinit var btnAddUsu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adm_tela_cadast_usu)

        rv = findViewById(R.id.rvUsuario)
        btnVoltar = findViewById(R.id.botaoVoltar4)
        btnLixeira = findViewById(R.id.btnExcProd3)
        btnAddUsu = findViewById(R.id.addUsu)

        rv.layoutManager = LinearLayoutManager(this)

        adapter = UsuarioAdapter(
            onEditar = { usuario ->
                val intent = Intent(this, AdmTelaInfoUsu::class.java)
                intent.putExtra("usuarioId", usuario.id)
                startActivity(intent)
            },
            onCheckedChange = { _, _ ->
                atualizarEstadoLixeira()
            }
        )

        rv.setHasFixedSize(true)
        rv.adapter = adapter

        adapter.submitList(
            listOf(
                Usuario(1, R.drawable.narak, "Thiago Narak 1"),
                Usuario(2, R.drawable.narak, "Thiago Narak 2"),
                Usuario(3, R.drawable.narak, "Thiago Narak 3")
            )
        )

        btnVoltar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnLixeira.setOnClickListener {
            val selecionados = adapter.getSelecionados()

            if (selecionados.isEmpty()) {
                val toast = Toast.makeText(this, "Selecione um item", Toast.LENGTH_SHORT)
                toast.view?.setBackgroundResource(android.R.color.holo_red_light)
                toast.show()
                return@setOnClickListener
            }

            val mensagem = if (selecionados.size == 1) {
                "Tem certeza que deseja excluir o usuário \"${selecionados.first().nome}\"?"
            } else {
                "Tem certeza que deseja excluir ${selecionados.size} usuários?"
            }

            MaterialAlertDialogBuilder(this)
                .setMessage(mensagem)
                .setPositiveButton("Sim") { _, _ ->
                    val idsExcluir = selecionados.map { it.id }.toSet()
                    val novaLista = adapter.currentList.filterNot { it.id in idsExcluir }
                    adapter.submitList(novaLista)
                    adapter.clearSelecao()

                    atualizarEstadoLixeira()
                }
                .setNegativeButton("Não", null)
                .show()
        }

        atualizarEstadoLixeira()

        btnAddUsu.setOnClickListener {
            val intent = Intent(this, AdmTelaCadastUsu::class.java)
            startActivity(intent)
        }
    }

    private fun atualizarEstadoLixeira() {
        val ativo = adapter.getSelecionados().isNotEmpty()
        btnLixeira.isEnabled = ativo
        btnLixeira.alpha = if (ativo) 1f else 0.4f
    }
}
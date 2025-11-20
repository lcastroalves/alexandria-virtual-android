    package com.example.alexandriavirtual20
    import android.graphics.Bitmap
    import android.graphics.drawable.BitmapDrawable
    import android.os.Bundle
    import android.widget.Button
    import android.widget.ImageButton
    import android.widget.ImageView
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.activity.result.ActivityResultLauncher
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.appcompat.app.AppCompatActivity
    import com.google.android.material.textfield.TextInputEditText
    import com.google.firebase.firestore.FirebaseFirestore
    import java.io.ByteArrayOutputStream
    import android.util.Base64
    import com.google.firebase.FirebaseApp
    import com.google.firebase.firestore.CollectionReference

    class AdmTelaCadasProd : AppCompatActivity() {

        private lateinit var btnVoltar: ImageButton
        private lateinit var btnMudarImg: ImageButton
        private lateinit var capaLivro: ImageView
        private lateinit var abrirGaleria: ActivityResultLauncher<String>
        private lateinit var btnCadastrar: Button

        private lateinit var titulo: TextInputEditText
        private lateinit var subtitulo: TextInputEditText
        private lateinit var autor: TextInputEditText
        private lateinit var categoria: TextInputEditText
        private lateinit var anoPub: TextInputEditText
        private lateinit var edicao: TextInputEditText
        private lateinit var unidade: TextInputEditText
        private lateinit var idioma: TextInputEditText
        private lateinit var sinopse: TextInputEditText
        private lateinit var fb: FirebaseFirestore

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.adm_tela_cadas_prod)
            FirebaseApp.initializeApp(this)

            btnVoltar = findViewById(R.id.botaoVoltar)
            btnMudarImg = findViewById(R.id.btnMudarImg)
            capaLivro = findViewById(R.id.capaEvento)
            btnCadastrar = findViewById(R.id.btnCadastrar)

            titulo = findViewById(R.id.titulo)
            subtitulo = findViewById(R.id.subtitulo)
            autor = findViewById(R.id.autor)
            categoria = findViewById(R.id.categoria)
            anoPub = findViewById(R.id.anoPub)
            edicao = findViewById(R.id.edicao)
            unidade = findViewById(R.id.unidade)
            idioma = findViewById(R.id.idioma)
            sinopse = findViewById(R.id.sinopse)

            fb = FirebaseFirestore.getInstance()

            abrirGaleria = registerForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri ->
                if (uri != null) {
                    capaLivro.setImageURI(uri)
                }
            }

            btnMudarImg.setOnClickListener {
                abrirGaleria.launch("image/*")
            }

            btnVoltar.setOnClickListener {
                // Cria o AlertDialog
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setTitle("Voltar")
                builder.setMessage("Deseja encerrar o cadastro?")

                builder.setPositiveButton("Sim") { dialog, _ ->
                    onBackPressedDispatcher.onBackPressed()
                    dialog.dismiss()
                }

                // Botão "Não"
                builder.setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss() // Apenas fecha o popup
                }

                val dialog = builder.create()
                dialog.show()
            }


            btnCadastrar.setOnClickListener {

                val tituloStr = titulo.text.toString().trim()
                val subtituloStr = subtitulo.text.toString().trim()
                val autorStr = autor.text.toString().trim()
                val categoriaStr = categoria.text.toString().trim()
                val anoPubStr = anoPub.text.toString().trim()
                val edicaoStr = edicao.text.toString().trim()
                val unidadeStr = unidade.text.toString().trim()
                val idiomaStr = idioma.text.toString().trim()
                val sinopseStr = sinopse.text.toString().trim()


                if (tituloStr.isEmpty() || subtituloStr.isEmpty() || autorStr.isEmpty() ||
                    categoriaStr.isEmpty() || anoPubStr.isEmpty() || edicaoStr.isEmpty() ||
                    unidadeStr.isEmpty() || idiomaStr.isEmpty() || sinopseStr.isEmpty()
                ) {
                    Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                // Converter imagem para Base64
                val imagemEnviada = (capaLivro.drawable as? BitmapDrawable)?.bitmap
                val imagemBase64 = if (imagemEnviada != null) {
                    val outputStream = ByteArrayOutputStream()
                    imagemEnviada.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
                } else {
                    ""
                }

                val produtos = fb.collection("livros")

                //Cria uma consulta (query) filtrando documentos da coleção. Aqui, estamos pedindo todos os documentos em que o campo "titulo" é igual ao valor de tituloStr.
                produtos.whereEqualTo("titulo", tituloStr).get().addOnSuccessListener { query ->
                    if (!query.isEmpty) {
                        Toast.makeText(this, "Produto já cadastrado!", Toast.LENGTH_LONG).show()
                    } else {
                        // Cria o mapa do produto
                        adicionarProduto(
                            produtos, tituloStr, subtituloStr, autorStr,
                            categoriaStr, anoPubStr, edicaoStr, unidadeStr, idiomaStr,
                            sinopseStr, imagemBase64
                        )
                    }
                }
            }
        }

        private fun adicionarProduto(
            produtos: CollectionReference,
            tituloStr: String,
            subtituloStr: String,
            autorStr: String,
            categoriaStr: String,
            anoPubStr: String,
            edicaoStr: String,
            unidadeStr: String,
            idiomaStr: String,
            sinopseStr: String,
            imagemBase64: String
        ) {
            val produto = mapOf(
                "titulo" to tituloStr,
                "subtitulo" to subtituloStr,
                "autor" to autorStr,
                "genero" to categoriaStr,
                "anoLancamento" to anoPubStr,
                "edicao" to edicaoStr,
                "unidade" to unidadeStr,
                "idioma" to idiomaStr,
                "sinopse" to sinopseStr,
                "capa" to imagemBase64
            )

            produtos.add(produto).addOnSuccessListener {
                Toast.makeText(this, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                // Limpar campos e imagem
                titulo.text?.clear()
                subtitulo.text?.clear()
                autor.text?.clear()
                categoria.text?.clear()
                anoPub.text?.clear()
                edicao.text?.clear()
                unidade.text?.clear()
                idioma.text?.clear()
                sinopse.text?.clear()
                capaLivro.setImageResource(0)
            }
        }
    }

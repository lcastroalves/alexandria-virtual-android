package com.example.alexandriavirtual20.model

data class Comentario(
    val idLivro: Int,            // identifica de qual livro é o comentário
    val nomeUsuario: String,
    val comentario: String,
    val estrelas: Int,
    val fotoRes: Int? = null
)

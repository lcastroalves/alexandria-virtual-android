package com.example.alexandriavirtual20.model

data class Comentario(
    val idLivro: String,
    val idUsu: String,// identifica de qual livro é o comentário
    val nomeUsuario: String,    //nao vai precisar
    val comentario: String,
    val estrelas: Int,
    val fotoRes: Int? = null    //nao vai precisar
)

package com.example.alexandriavirtual20.model

// **NÃO precisa de @PropertyName se você estiver SALVANDO com os nomes abaixo:**
// Ex: Se você SALVA {"estrelas": 5}, a propriedade deve ser 'val estrelas: Int'

data class Comentario(

    val idLivro: String = "",

    // Mapeia o campo do Firebase "idUsu"
    val idUsu: String = "",

    // Mapeia o campo do Firebase "comentario"
    val comentario: String = "",

    // Mapeia o campo do Firebase "estrelas"
    val estrelas: Int = 0,

    // Mapeia o campo do Firebase "nomeUsuario"
    val nomeUsuario: String? = null,

    // Outros campos
    val fotoRes: Int? = null,
    val timestamp: Long = 0L
)
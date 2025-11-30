package com.example.alexandriavirtual20.model

import com.google.firebase.Timestamp

data class Emprestimo (
    val id: String = "",
    val idLivro: String = "",
    val idUsuario: String = "",
    val dataPrevistaDevolucao: Timestamp? = null,
    val dataDevolucao: Timestamp? = null,
    val statusEmprestimo: String = "pendente",   // pendente, negado ou aceito
    val statusDevolucao : String = "devolvido"// devolvido, atrasado

)
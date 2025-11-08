package com.example.alexandriavirtual20.model

data class Usuario(
    val id: String,
    val nome: String,
    val usuario: String = " ",
    val email: String = " ",
    val fotoPerfil: String?,
    val senha: String = ""
)
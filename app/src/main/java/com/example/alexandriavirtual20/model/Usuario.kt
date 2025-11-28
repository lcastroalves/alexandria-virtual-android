package com.example.alexandriavirtual20.model

data class Usuario(
    val id: String = "",
    val nome: String = "",
    val usuario: String = "",
    val email: String = "",
    val fotoPerfil: String? = null,
    val admin: Boolean = false
)
package com.example.alexandriavirtual20.model


import androidx.annotation.DrawableRes

data class Usuario(
    val id: String,
    val nome: String,
    val usuario: String = " ",
    val email: String = " ",
    @DrawableRes val fotoPerfil: Int,
)
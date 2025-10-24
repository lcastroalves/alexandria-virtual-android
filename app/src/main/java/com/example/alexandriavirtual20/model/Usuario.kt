package com.example.alexandriavirtual20.model


import androidx.annotation.DrawableRes

data class Usuario(
    val id: Long,
    @DrawableRes val fotoPerfil: Int,
    val nome: String,
)
package com.example.alexandriavirtual20.model

import com.example.alexandriavirtual20.R

data class Cabine (
    val id: Int,
    val nome: String,
    val bloco: String,
    val iconRes: Int = R.drawable.livro_cab
)
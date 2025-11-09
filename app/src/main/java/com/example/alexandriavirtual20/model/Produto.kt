package com.example.alexandriavirtual20.model

// data vai permitir retornar muitos metodos diferentes automaticamente

data class Produto (
    var titulo : String,
    var autor : String,
    var imageBase64: String,
    var isSelected: Boolean = false
)

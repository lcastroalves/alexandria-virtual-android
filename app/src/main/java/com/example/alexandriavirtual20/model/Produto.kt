package com.example.alexandriavirtual20.model

data class Produto (
    var titulo : String,
    var autor : String,
    var imageBase64: String,
    var isSelected: Boolean = false
)

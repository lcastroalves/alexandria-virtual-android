package com.example.alexandriavirtual20.model

data class Evento (
    var nome : String,
    var data : String,
    var horario : String,
    var descricao : String,
    var breveDescricao : String,
    var imagem : Int,
    var isSelected: Boolean = false
    )
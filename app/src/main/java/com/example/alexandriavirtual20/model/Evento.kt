package com.example.alexandriavirtual20.model

data class Evento (
    var imagem : String,
    var nome : String,
    var data : String,
    var horario : String,
    var descricao : String,
    var breveDescricao : String,
    var local : String,
    var isSelected: Boolean = false
    )
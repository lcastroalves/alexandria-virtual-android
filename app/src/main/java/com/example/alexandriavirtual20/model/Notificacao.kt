package com.example.alexandriavirtual20.model

data class Notificacao (
    var id: String,
    var nome : String,
    var tipo : String,
    var imagem : String,
    var data : String,
    var prazo : Int,
    var dias : Int,
    var mensagem : String
)
package com.example.alexandriavirtual20.model

import java.sql.Timestamp

data class Notificacao (
    var nome : String,
    var tipo : String,
    var imagem : String,
    var data : String,
    var prazo : Int,
    var dias : Int,
    var mensagem : String
    )
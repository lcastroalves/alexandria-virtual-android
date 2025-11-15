package com.example.alexandriavirtual20.adapter

import com.example.alexandriavirtual20.model.Evento

data class Notificacao (
    var nome: String,
    var data: String, // nome, data e imagem pegará do objeto referente a notificação
    var imagem: Int,
    var tipo: String, // tipo evento caso um objeto evento o tenha criado ou tipo devolução caso um objeto produto o tenha criado
    var mensagem: String, // mensagem será diferente dependendo do prazo
    var prazo: Int // prazo será o tempo até a data em dias

)
//{
//    init {
//        if (objeto is Evento) {
//            this.tipo = "Evento"
//        } else {
//            this.tipo = "Devolução"
//        }
//    }
//}

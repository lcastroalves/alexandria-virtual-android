package com.example.alexandriavirtual20.model

import com.google.firebase.firestore.DocumentId

data class Cabine (
    @DocumentId
    var id: String = " ",
    var numero: String = " ",
    var dia: String = " ",
    var aluno: String = " ",
    var inicio: String = " ",
    var fim: String = " ",
    var livre: Boolean = true
)
package com.example.alexandriavirtual20.model

import android.os.Parcel
import android.os.Parcelable

data class Livro(
    val id: String = "",               // ID do documento no Firebase
    val titulo: String = "",
    val subtitulo: String = "",
    val autor: String = "",
    val imagem: Int,             // Se quiser usar URL depois, podemos trocar p/ String
    val avaliacoes: String = "",          // Agora é INT (correto para contagem)
    var favorito: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",     // id
        parcel.readString() ?: "",     // titulo
        parcel.readString() ?: "",     // subtitulo
        parcel.readString() ?: "",     // autor
        parcel.readInt(),              // imagem
        parcel.readString() ?: "",              // avaliacoes
        parcel.readByte() != 0.toByte() // favorito
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(titulo)
        parcel.writeString(subtitulo)
        parcel.writeString(autor)
        parcel.writeInt(imagem)
        parcel.writeString(avaliacoes)
        parcel.writeByte(if (favorito) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Livro> {
        override fun createFromParcel(parcel: Parcel): Livro = Livro(parcel)
        override fun newArray(size: Int): Array<Livro?> = arrayOfNulls(size)
    }
}

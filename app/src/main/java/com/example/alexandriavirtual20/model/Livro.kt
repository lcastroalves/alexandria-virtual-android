package com.example.alexandriavirtual20.model

import android.os.Parcel
import android.os.Parcelable

data class Livro(
    val id: String = "",
    val titulo: String = "",
    val genero: String = "",
    val autor: String = "",
    var imageBase64: String = "",
    val avaliacoes: Int = 0,
    var favorito: Boolean = false,
    val anoLancamento: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",      // id
        parcel.readString() ?: "",      // titulo
        parcel.readString() ?: "",      // genero
        parcel.readString() ?: "",      // autor
        parcel.readString() ?: "",      // imageBase64
        parcel.readInt(),               // avaliacoes
        parcel.readByte() != 0.toByte(),// favorito
        parcel.readString() ?: ""       // anoLancamento
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(titulo)
        parcel.writeString(genero)
        parcel.writeString(autor)
        parcel.writeString(imageBase64)
        parcel.writeInt(avaliacoes)
        parcel.writeByte(if (favorito) 1 else 0)
        parcel.writeString(anoLancamento)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Livro> {
        override fun createFromParcel(parcel: Parcel): Livro = Livro(parcel)
        override fun newArray(size: Int): Array<Livro?> = arrayOfNulls(size)
    }
}


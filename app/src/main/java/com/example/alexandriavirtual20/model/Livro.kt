package com.example.alexandriavirtual20.model

import android.os.Parcel
import android.os.Parcelable

data class Livro(
    val titulo: String,
    val autor: String,
    val imagem: Int,
    val avaliacao: String
)
    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(titulo)
        parcel.writeString(autor)
        parcel.writeInt(imagem)
        parcel.writeString(avaliacao)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Livro> {
        override fun createFromParcel(parcel: Parcel): Livro = Livro(parcel)
        override fun newArray(size: Int): Array<Livro?> = arrayOfNulls(size)
    }
}

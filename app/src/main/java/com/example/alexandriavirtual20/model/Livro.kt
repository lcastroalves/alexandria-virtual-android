package com.example.alexandriavirtual20.model

import android.os.Parcel
import android.os.Parcelable

data class Livro(
    val id: String = "",
    val titulo: String = "",
    val subtitulo: String = "",
    val genero: String = "",
    val autor: String = "",
    var imageBase64: String = "",
    val avaliacoes: Int = 0,
    var favorito: Boolean = false,
    val anoLancamento: String = "",
    val sinopse: String = "",
    val idioma: String = "",
    val edicao: String = "",
    val unidade: String = "",
    var isSelected: Boolean = false
) : Parcelable {

    // construtor secundario para usar na tela de inicio
    constructor(
        titulo: String,
        autor: String,
        imageBase64: String,
        isSelected: Boolean
    ) : this(
        id = "",
        titulo = titulo,
        subtitulo = "",
        genero = "",
        autor = autor,
        imageBase64 = imageBase64,
        avaliacoes = 0,
        favorito = false,
        anoLancamento = "",
        sinopse = "",
        idioma = "",
        edicao = "",
        unidade = "",
        isSelected = isSelected
    )

    // construtor padrao
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(titulo)
        parcel.writeString(subtitulo)
        parcel.writeString(genero)
        parcel.writeString(autor)
        parcel.writeString(imageBase64)
        parcel.writeInt(avaliacoes)
        parcel.writeByte(if (favorito) 1 else 0)
        parcel.writeString(anoLancamento)
        parcel.writeString(sinopse)
        parcel.writeString(idioma)
        parcel.writeString(edicao)
        parcel.writeString(unidade)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Livro> {
        override fun createFromParcel(parcel: Parcel): Livro = Livro(parcel)
        override fun newArray(size: Int): Array<Livro?> = arrayOfNulls(size)
    }
}

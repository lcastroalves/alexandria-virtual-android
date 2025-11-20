package com.example.alexandriavirtual20.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LivroCompartilhadoViewModel : ViewModel(){
    val livroSelecionado = MutableLiveData<Livro>()

    fun selecionarLivro(livro: Livro){
        livroSelecionado.value = livro
    }
}
// HistoricoEmprestimo.kt
package com.example.alexandriavirtual20.model

data class HistoricoEmprestimo(
    // Dados do Empréstimo (Coleção 'emprestimo')
    val idDocumento: String,            // ID do documento do Firestore
    val idLivro: String,                // Para a busca do Livro
    val idUsuario: String,              // Para a busca do Usuário

    val dataEmprestimo: String,         // Data formatada de quando o livro foi retirado (Ex: "09 de Set")
    val dataPrevistaDevolucao: String,  // Data formatada do campo 'prazo' do Firestore
    val statusDevolucao: String,        // Ex: "Em dia", "Atrasado", "Devolvido" (Assumindo que este campo é adicionado ao 'emprestimo')

    // Dados do Livro (Carregados após a busca)
    val titulo: String,
    val autor: String,
    val capa: String,

    // Dados do Usuário (Carregados após a busca)
    val nomeUsuario: String,
    val emailUsuario: String
)
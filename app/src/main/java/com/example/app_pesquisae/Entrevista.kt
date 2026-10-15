package com.example.app_pesquisae

// Guarda, de forma TEMPORÁRIA, as respostas do entrevistado enquanto ele
// passa pelas telas (Espontânea -> Estimulada -> Problemas -> Dados).
// Só no final (botão Enviar da tela de Dados) tudo é gravado no banco.
// "object" = existe uma única cópia, que todas as telas enxergam.
object Entrevista {

    var espontanea: String = ""            // nome digitado na pesquisa espontânea
    var estimulada: String = ""            // opção escolhida na pesquisa estimulada
    var problemas: List<String> = listOf() // os 3 problemas marcados
    var nome: String = ""
    var celular: String = ""

    // Zera tudo para começar um novo entrevistado
    fun limpar() {
        espontanea = ""
        estimulada = ""
        problemas = listOf()
        nome = ""
        celular = ""
    }
}
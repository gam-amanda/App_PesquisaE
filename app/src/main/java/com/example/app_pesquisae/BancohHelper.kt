package com.example.app_pesquisae

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Uma linha da tabela, usada para mostrar a lista de eleitores
data class Registro(
    val nome: String,
    val celular: String,
    val cidade: String,
    val dataHora: String,
    val latitude: Double?,   // null = localização não disponível
    val longitude: Double?
)

// Banco de dados local (SQLite). Fica em um arquivo dentro do próprio app.
class BancoHelper(context: Context) :
    SQLiteOpenHelper(context, "pesquisa.db", null, 1) {

    // Chamado só na primeira vez: cria a tabela
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE entrevistas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                espontanea TEXT NOT NULL,
                estimulada TEXT NOT NULL,
                problema1 TEXT NOT NULL,
                problema2 TEXT NOT NULL,
                problema3 TEXT NOT NULL,
                nome TEXT NOT NULL,
                celular TEXT NOT NULL,
                cidade TEXT NOT NULL,
                data_hora TEXT NOT NULL,
                latitude REAL,
                longitude REAL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS entrevistas")
        onCreate(db)
    }
    // RF02, RF03, RF04: grava UMA entrevista completa (uma linha)
    fun salvar(cidade: String, dataHora: String, latitude: Double?, longitude: Double?) {
        val valores = ContentValues()
        valores.put("espontanea", Entrevista.espontanea)
        valores.put("estimulada", Entrevista.estimulada)
        valores.put("problema1", Entrevista.problemas[0])
        valores.put("problema2", Entrevista.problemas[1])
        valores.put("problema3", Entrevista.problemas[2])
        valores.put("nome", Entrevista.nome)
        valores.put("celular", Entrevista.celular)
        valores.put("cidade", cidade)
        valores.put("data_hora", dataHora)
        if (latitude != null && longitude != null) {
            valores.put("latitude", latitude)
            valores.put("longitude", longitude)
        } else {
            valores.putNull("latitude")
            valores.putNull("longitude")
        }
        writableDatabase.insert("entrevistas", null, valores)
    }
}

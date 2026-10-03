package com.example.app_pesquisae

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultadosActivity : AppCompatActivity() {

    private lateinit var btVoltar : Button
    private lateinit var tvQtdPessoas : TextView
    private lateinit var llLegenda : LinearLayout
    private lateinit var tvSemDados : TextView
    private lateinit var pieVotos : PieChartView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultados)

        btVoltar = findViewById(R.id.btVoltar)
        tvQtdPessoas = findViewById(R.id.tvQtdPessoas)
        llLegenda = findViewById(R.id.llLegenda)
        tvSemDados = findViewById(R.id.tvSemDados)
        pieVotos = findViewById(R.id.pieVotos)

        btVoltar.setOnClickListener {
            finish()
        }

        mostrarResultados()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // RF06: busca os números no banco e mostra legenda e pizza
    private fun mostrarResultados() {
        var banco = BancoHelper(this)
        var total = banco.contar()
        tvQtdPessoas.text = total.toString()

        var votos = banco.contarVotos()

        if (total == 0) {
            tvSemDados.visibility = View.VISIBLE
            pieVotos.visibility = View.GONE
        } else {
            // legenda: "● Candidato 1 - 40 - 50%" (bolinha na cor da fatia)
            for (i in votos.indices) {
                var porcento = Math.round(100f * votos[i].second / total)
                var texto = "●  " + votos[i].first + " - " + votos[i].second + " - " + porcento + "%"
                var span = SpannableString(texto)
                span.setSpan(
                    ForegroundColorSpan(PieChartView.CORES[i % PieChartView.CORES.size]),
                    0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                var linha = TextView(this)
                linha.text = span
                linha.setTextColor(Color.BLACK)
                linha.setPadding(0, 4, 0, 4)
                llLegenda.addView(linha)
            }
            pieVotos.definirDados(votos)
        }
    }
}
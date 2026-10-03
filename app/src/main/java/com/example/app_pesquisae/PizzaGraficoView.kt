package com.example.app_pesquisae

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

// Gráfico de pizza desenhado "à mão" com Canvas (sem biblioteca externa).
// Uso: pie.definirDados(listOf(Pair("Candidato 1", 40), Pair("Nulo", 10)))
class PieChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        // Cores das fatias (a legenda da tela usa as mesmas, na mesma ordem)
        val CORES = intArrayOf(
            Color.parseColor("#4285F4"), Color.parseColor("#DB4437"),
            Color.parseColor("#F4B400"), Color.parseColor("#0F9D58"),
            Color.parseColor("#AB47BC"), Color.parseColor("#00ACC1"),
            Color.parseColor("#FF7043"), Color.parseColor("#9E9E9E")
        )
    }

    private var dados: List<Pair<String, Int>> = listOf()
    private val pincel = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pincelTexto = Paint(Paint.ANTI_ALIAS_FLAG)
    private val area = RectF()

    init {
        pincelTexto.color = Color.WHITE
        pincelTexto.textSize = 36f
        pincelTexto.textAlign = Paint.Align.CENTER
        pincelTexto.isFakeBoldText = true
    }

    fun definirDados(novos: List<Pair<String, Int>>) {
        dados = novos
        invalidate()   // pede para a tela desenhar de novo
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val total = dados.sumOf { it.second }
        if (total == 0) return

        // quadrado centralizado dentro da view
        val lado = minOf(width, height).toFloat() - 8f
        val esq = (width - lado) / 2f
        val topo = (height - lado) / 2f
        area.set(esq, topo, esq + lado, topo + lado)

        var inicio = -90f   // começa no topo
        for (i in dados.indices) {
            val varredura = 360f * dados[i].second / total
            pincel.color = CORES[i % CORES.size]
            canvas.drawArc(area, inicio, varredura, true, pincel)

            // porcentagem escrita no meio da fatia (só se couber)
            if (varredura >= 25f) {
                val meio = Math.toRadians((inicio + varredura / 2f).toDouble())
                val x = area.centerX() + (lado / 2f * 0.62f) * Math.cos(meio).toFloat()
                val y = area.centerY() + (lado / 2f * 0.62f) * Math.sin(meio).toFloat() + 12f
                val pct = Math.round(100f * dados[i].second / total)
                canvas.drawText("$pct%", x, y, pincelTexto)
            }
            inicio += varredura
        }
    }
}
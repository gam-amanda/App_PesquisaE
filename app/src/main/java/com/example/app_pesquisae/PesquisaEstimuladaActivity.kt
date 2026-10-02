package com.example.app_pesquisae

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PesquisaEstimuladaActivity : AppCompatActivity() {
    private lateinit var btVoltar : Button
    private lateinit var btEnviar : Button
    private lateinit var checkboxes: List<CheckBox>
    //criamps uma var lista do tipo checkbox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesquisa_estimulada)

        btVoltar = findViewById(R.id.btVoltar)
        btEnviar = findViewById(R.id.btEnviar)

        //lista de checkbox
        val cbC1 = findViewById<CheckBox>(R.id.cbC1)
        val cbC2 = findViewById<CheckBox>(R.id.cbC2)
        val cbC3 = findViewById<CheckBox>(R.id.cbC3)
        val cbC4 = findViewById<CheckBox>(R.id.cbC4)
        val cbC5 = findViewById<CheckBox>(R.id.cbC5)
        val cbBranco = findViewById<CheckBox>(R.id.cbBranco)
        val cbNulo = findViewById<CheckBox>(R.id.cbNulo)
        val cbNaoSei = findViewById<CheckBox>(R.id.cbNaoSei)

        //Agrupamos todos numa lista para facilitar o controle
                checkboxes = listOf(cbC1, cbC2, cbC3, cbC4, cbC5, cbBranco, cbNulo, cbNaoSei)

        //Aplicando a lógica de seleção única (quando um for marcado, os outros desmarcam)
        for (checkbox in checkboxes) {
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // Se este foi marcado, percorre os outros desmarcando-os
                    for (outro in checkboxes) {
                        if (outro != buttonView) {
                            outro.isChecked = false
                        }
                    }
                }
            }
        }

        btVoltar.setOnClickListener {
            finish()
        }

        btEnviar.setOnClickListener {
            var telaPesquisaProblemas :  Intent
            telaPesquisaProblemas = Intent(this, PesquisaProblemasActivity::class.java)
            startActivity(telaPesquisaProblemas)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
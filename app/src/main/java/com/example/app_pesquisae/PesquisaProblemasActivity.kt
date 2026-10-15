package com.example.app_pesquisae

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.CheckBox
import android.widget.Toast

class PesquisaProblemasActivity : AppCompatActivity() {
    private lateinit var btVoltar : Button
    private lateinit var btEnviar : Button

    private lateinit var checkboxes : List<CheckBox>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesquisa_problemas)

        btVoltar = findViewById(R.id.btVoltar)
        btEnviar = findViewById(R.id.btEnviar)

        //agrupamos os 10 problemas numa lista para facilitar o controle
        checkboxes = listOf(
            findViewById<CheckBox>(R.id.cbP1), findViewById<CheckBox>(R.id.cbP2),
            findViewById<CheckBox>(R.id.cbP3), findViewById<CheckBox>(R.id.cbP4),
            findViewById<CheckBox>(R.id.cbP5), findViewById<CheckBox>(R.id.cbP6),
            findViewById<CheckBox>(R.id.cbP7), findViewById<CheckBox>(R.id.cbP8),
            findViewById<CheckBox>(R.id.cbP9), findViewById<CheckBox>(R.id.cbP10)
        )

        //Limite: no máximo 3 problemas marcados
        for (checkbox in checkboxes) {
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked && contarMarcados() > 3) {
                    // era o 4º marcado: desmarca e avisa
                    buttonView.isChecked = false
                    Toast.makeText(this, "Escolha no máximo 3 problemas", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btVoltar.setOnClickListener {
            finish()
        }
        btEnviar.setOnClickListener {
            //RF03: precisa ter exatamente 3 problemas marcados
            if (contarMarcados() != 3) {
                Toast.makeText(this, "Escolha exatamente 3 problemas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //guarda o texto de cada caixinha marcada
            var escolhidos = mutableListOf<String>()
            for (checkbox in checkboxes) {
                if (checkbox.isChecked) {
                    escolhidos.add(checkbox.text.toString())
                }
            }
            Entrevista.problemas = escolhidos

            var telaDadosEleitor : Intent
            telaDadosEleitor = Intent(this, DadosEleitorActivity::class.java)
            startActivity(telaDadosEleitor)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //conta quantas caixinhas estão marcadas
    private fun contarMarcados(): Int {
        var total = 0
        for (checkbox in checkboxes) {
            if (checkbox.isChecked) {
                total++
            }
        }
        return total
    }
}
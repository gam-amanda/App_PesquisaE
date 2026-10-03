package com.example.app_pesquisae

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.Toast

class PesquisaEspontaneaActivity : AppCompatActivity() {

    private lateinit var btSair : Button
    private lateinit var btEnviar : Button

    private lateinit var etCandidato : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesquisa_espontanea)

        btSair = findViewById(R.id.btVoltar)
        btEnviar = findViewById(R.id.btEnviar)
        etCandidato = findViewById(R.id.etCandidato)

        btSair.setOnClickListener {
            finish()
        }
        btEnviar.setOnClickListener {
            //RF02: guarda o nome digitado (precisa ter algo escrito)
            var nome = etCandidato.text.toString().trim()
            if (nome.isEmpty()) {
                Toast.makeText(this, "Digite o nome de um candidato", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Entrevista.espontanea = nome
            var telaPesquisaEstimulada : Intent
            telaPesquisaEstimulada = Intent(this, PesquisaEstimuladaActivity::class.java)
            startActivity(telaPesquisaEstimulada)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
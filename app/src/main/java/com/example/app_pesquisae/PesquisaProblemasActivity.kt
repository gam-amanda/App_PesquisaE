package com.example.app_pesquisae

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PesquisaProblemasActivity : AppCompatActivity() {
    private lateinit var btVoltar : Button
    private lateinit var btEnviar : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesquisa_problemas)

        btVoltar = findViewById(R.id.btVoltar)
        btEnviar = findViewById(R.id.btEnviar)

        btVoltar.setOnClickListener {
            finish()
        }
        btEnviar.setOnClickListener {
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
}
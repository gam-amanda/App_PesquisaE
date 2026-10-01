package com.example.app_pesquisae

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdminMenuActivity : AppCompatActivity() {

    //criando variaveis
    private lateinit var btSair : Button
    private lateinit var tvQtdPessoas : TextView
    private lateinit var btEleitores : Button
    private lateinit var btResultados : Button
    private lateinit var btLimparDados : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_menu)

        btSair = findViewById(R.id.btSair)
        tvQtdPessoas = findViewById(R.id.tvQtdPessoas)
        btEleitores = findViewById(R.id.btEleitores)
        btResultados = findViewById(R.id.btResultados)
        btLimparDados = findViewById(R.id.btLimparDados)

        btSair.setOnClickListener {
            finish()
        }

        btEleitores.setOnClickListener {
            //var intenção de tela
            var telaEleitores : Intent
            telaEleitores = Intent(this, EleitoresActivity::class.java)
            startActivity(telaEleitores)
        }

        btResultados.setOnClickListener {
            //var intenção de tela
            var telaResultados : Intent
            telaResultados = Intent(this, ResultadosActivity::class.java)
            startActivity(telaResultados)
        }

        btLimparDados.setOnClickListener {

        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
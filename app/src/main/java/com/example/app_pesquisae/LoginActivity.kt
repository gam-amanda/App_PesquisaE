package com.example.app_pesquisae

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    //criação de variaveis com o nome do botão
    private lateinit var btSair : Button
    private lateinit var etUser : EditText
    private lateinit var etSenha: EditText
    private lateinit var btEntrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        btSair = findViewById(R.id.btSair)
        btEntrar = findViewById(R.id.btEntrar)

        etUser = findViewById(R.id.etUser)
        etSenha = findViewById(R.id.etSenha)

        btEntrar.setOnClickListener {
            var user = etUser.text.toString()
            var senha = etSenha.text.toString()

            //Var intenção de tela
            var telaAdminMenu : Intent
            telaAdminMenu = Intent(this, AdminMenuActivity::class.java)

            var telaPesquisaEsp : Intent
            telaPesquisaEsp = Intent(this, PesquisaEspontaneaActivity::class.java)

            //Lógica de direcionamento
            //login: admin         | senha: admin         --> administrador
            //login: entrevistador | senha: entrevistador --> entrevistador

            if (user == "admin" && senha == "admin"){
                startActivity(telaAdminMenu)
            }
            else if (user == "entrevistador" && senha == "entrevistador"){
                startActivity(telaPesquisaEsp)
            } else {
                Toast.makeText(this, "Credenciais inválidas!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }

        btSair.setOnClickListener {
            finishAndRemoveTask()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
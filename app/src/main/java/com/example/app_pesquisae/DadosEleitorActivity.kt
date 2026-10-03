package com.example.app_pesquisae

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DadosEleitorActivity : AppCompatActivity() {
    private lateinit var btVoltar : Button
    private lateinit var btEnviar : Button
    private lateinit var etNomeUser : EditText
    private lateinit var etCelular : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dados_eleitor)

        btVoltar = findViewById(R.id.btVoltar)
        btEnviar = findViewById(R.id.btEnviar)
        etNomeUser = findViewById(R.id.etNomeUser)
        etCelular = findViewById(R.id.editTextNumber)

        btVoltar.setOnClickListener {
            finish()
        }

        btEnviar.setOnClickListener {
            var nome = etNomeUser.text.toString().trim()
            var celular = etCelular.text.toString().trim()

            //RF04: validações
            if (nome.isEmpty()) {
                Toast.makeText(this, "Digite o nome do entrevistado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (celular.length != 10 && celular.length != 11) {
                Toast.makeText(this, "Celular inválido (use DDD + número)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Entrevista.nome = nome
            Entrevista.celular = celular

            //data e hora do sistema
            var dataHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date())

            //grava no banco (cidade e localização vamos acrescentar no próximo passo)
            var banco = BancoHelper(this)
            banco.salvar("Não identificada", dataHora, null, null)

            //zera as respostas para o próximo entrevistado
            Entrevista.limpar()
            Toast.makeText(this, "Entrevista salva com sucesso!", Toast.LENGTH_LONG).show()

            //CLEAR_TOP: fecha as telas de cima (Estimulada, Problemas, Dados) e
            //abre uma Espontânea nova e vazia para o próximo entrevistado
            var telaPesquisaEspontanea : Intent
            telaPesquisaEspontanea = Intent(this, PesquisaEspontaneaActivity::class.java)
            telaPesquisaEspontanea.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(telaPesquisaEspontanea)
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
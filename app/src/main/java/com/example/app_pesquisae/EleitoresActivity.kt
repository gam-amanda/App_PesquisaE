package com.example.app_pesquisae

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EleitoresActivity : AppCompatActivity() {

    private lateinit var btVoltar : Button
    private lateinit var tvQtdPessoas : TextView
    private lateinit var lvEleitores : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eleitores)

        btVoltar = findViewById(R.id.btVoltar)
        tvQtdPessoas = findViewById(R.id.tvQtdPessoas)
        lvEleitores = findViewById(R.id.lvEleitores)

        btVoltar.setOnClickListener {
            finish()
        }

        //RF07: busca os entrevistados no banco e mostra na lista
        var lista = BancoHelper(this).listar()
        tvQtdPessoas.text = lista.size.toString()
        lvEleitores.adapter = EleitorAdapter(this, lista)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

// Preenche cada linha da lista (item_eleitor.xml) com os dados de um Registro
class EleitorAdapter(context: Context, lista: List<Registro>) :
    ArrayAdapter<Registro>(context, 0, lista) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val linha = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_eleitor, parent, false)
        val r = getItem(position)!!

        linha.findViewById<TextView>(R.id.tvItemNome).text = r.nome
        linha.findViewById<TextView>(R.id.tvItemCelular).text = "Celular: " + r.celular
        linha.findViewById<TextView>(R.id.tvItemCidade).text = "Cidade: " + r.cidade
        linha.findViewById<TextView>(R.id.tvItemDataHora).text = "Data e hora: " + r.dataHora

        var local = "Localização: não disponível"
        if (r.latitude != null && r.longitude != null) {
            local = "Localização: " + String.format(java.util.Locale.US, "%.5f, %.5f", r.latitude, r.longitude)
        }
        linha.findViewById<TextView>(R.id.tvItemLocalizacao).text = local
        return linha
    }
}
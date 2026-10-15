package com.example.app_pesquisae

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.os.CancellationSignal
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

    // Pede a permissão de localização. Qualquer que seja a resposta (sim ou não),
    // a entrevista é finalizada em seguida (sem permissão, salva sem localização).
    private val pedirPermissao = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        finalizar()
    }

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

            //se já temos permissão, finaliza; senão pede primeiro
            if (temPermissao()) {
                finalizar()
            } else {
                pedirPermissao.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun temPermissao(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Passo final: pega a localização, descobre a cidade e grava no banco
    private fun finalizar() {
        btEnviar.isEnabled = false   // evita clicar duas vezes
        Toast.makeText(this, "Salvando entrevista...", Toast.LENGTH_SHORT).show()

        obterLocalizacao { local ->
            //data e hora do sistema
            var dataHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(Date())

            if (local == null) {
                gravar("Não identificada", dataHora, null, null)
            } else {
                //descobrir a cidade pode demorar (internet), então roda em segundo plano
                Thread {
                    var cidade = descobrirCidade(local.latitude, local.longitude)
                    runOnUiThread {
                        gravar(cidade, dataHora, local.latitude, local.longitude)
                    }
                }.start()
            }
        }
    }

    // Grava no banco, limpa as respostas e volta para a Espontânea
    private fun gravar(cidade: String, dataHora: String, lat: Double?, lon: Double?) {
        var banco = BancoHelper(this)
        banco.salvar(cidade, dataHora, lat, lon)

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

    // Tenta pegar a posição atual (até 10 segundos). Se não conseguir, devolve null.
    @SuppressLint("MissingPermission")
    private fun obterLocalizacao(resposta: (Location?) -> Unit) {
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager

        //escolhe GPS ou, se estiver desligado, a rede
        var provedor: String? = null
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provedor = LocationManager.GPS_PROVIDER
        } else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provedor = LocationManager.NETWORK_PROVIDER
        }

        if (!temPermissao() || provedor == null) {
            resposta(null)
            return
        }

        var respondeu = false
        val cancelar = CancellationSignal()
        val entregar = { local: Location? ->
            if (!respondeu) {
                respondeu = true
                cancelar.cancel()
                resposta(local)
            }
        }

        LocationManagerCompat.getCurrentLocation(
            lm, provedor, cancelar, ContextCompat.getMainExecutor(this)
        ) { local -> entregar(local) }

        //se demorar mais de 10s, usa a última posição conhecida (ou null)
        Handler(Looper.getMainLooper()).postDelayed({
            entregar(lm.getLastKnownLocation(provedor))
        }, 10000)
    }

    // Converte latitude/longitude em nome de cidade
    @Suppress("DEPRECATION")
    private fun descobrirCidade(lat: Double, lon: Double): String {
        try {
            var enderecos = Geocoder(this, Locale("pt", "BR")).getFromLocation(lat, lon, 1)
            if (enderecos != null && enderecos.isNotEmpty()) {
                var e = enderecos[0]
                return e.locality ?: e.subAdminArea ?: "Não identificada"
            }
        } catch (erro: Exception) {
            //sem internet ou erro no serviço: segue sem a cidade
        }
        return "Não identificada"
    }
}
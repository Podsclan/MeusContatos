package br.edu.ifsp.scl.ads.s5.pdm.agenda.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.s5.pdm.agenda.adapter.AutenticadorFirebase
import br.edu.ifsp.scl.ads.s5.pdm.agenda.databinding.ActivityContatoBinding
import br.edu.ifsp.scl.ads.s5.pdm.agenda.model.Contato

class ContatoActivity : AppCompatActivity() {
    // Classe de ViewBinding
    private lateinit var activityContatoBinding: ActivityContatoBinding

    private val userId = AutenticadorFirebase.firebaseAuth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityContatoBinding = ActivityContatoBinding.inflate(layoutInflater)
        setContentView(activityContatoBinding.root)

        // Novo contato ou editar contato
        val contato: Contato? = intent.getParcelableExtra(MainActivity.Extras.EXTRA_CONTATO)
        if (contato != null) {
            // Editar contato
            activityContatoBinding.nomeContatoEt.setText(contato.nome)
            activityContatoBinding.nomeContatoEt.isEnabled = false
            activityContatoBinding.telefoneContatoEt.setText(contato.telefone)
            activityContatoBinding.emailContatoEt.setText(contato.email)
            if (intent.action == MainActivity.Extras.VISUALIZAR_CONTATO_ACTION) {
                // Visualizar contato
                activityContatoBinding.telefoneContatoEt.isEnabled = false
                activityContatoBinding.emailContatoEt.isEnabled = false
                activityContatoBinding.salvarBt.visibility = View.GONE
            }
        }

        activityContatoBinding.salvarBt.setOnClickListener {
            if(userId != null) {
                val novoContato = Contato(
                        activityContatoBinding.nomeContatoEt.text.toString(),
                        userId,
                        activityContatoBinding.telefoneContatoEt.text.toString(),
                        activityContatoBinding.emailContatoEt.text.toString()
                )

                val retornoIntent = Intent()
                retornoIntent.putExtra(MainActivity.Extras.EXTRA_CONTATO, novoContato)
                setResult(RESULT_OK, retornoIntent)
                finish()
            }
        }
    }
}
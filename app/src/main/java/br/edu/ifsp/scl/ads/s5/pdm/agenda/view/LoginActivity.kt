package br.edu.ifsp.scl.ads.s5.pdm.agenda.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.edu.ifsp.scl.ads.s5.pdm.agenda.R
import br.edu.ifsp.scl.ads.s5.pdm.agenda.adapter.AutenticadorFirebase
import br.edu.ifsp.scl.ads.s5.pdm.agenda.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    // View binding
    private lateinit var activityLoginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciando view binding
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(activityLoginBinding.root)
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.cadastrarBt -> {
                startActivity(Intent(this, CadastrarActivity::class.java))
            }
            R.id.entrarBt -> {
                val email = activityLoginBinding.emailEt.text.toString()
                val senha = activityLoginBinding.senhaEt.text.toString()
                AutenticadorFirebase.firebaseAuth.signInWithEmailAndPassword(email, senha).addOnSuccessListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Falha na autenticação do usuário.", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.recuperarSenhaBt -> {
                startActivity(Intent(this, RecuperarSenhaActivity::class.java))
            }
        }
    }
}
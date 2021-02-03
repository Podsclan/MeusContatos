package br.edu.ifsp.scl.ads.s5.pdm.agenda.view

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.ads.s5.pdm.agenda.R
import br.edu.ifsp.scl.ads.s5.pdm.agenda.adapter.AutenticadorFirebase
import br.edu.ifsp.scl.ads.s5.pdm.agenda.adapter.ContatosAdapter
import br.edu.ifsp.scl.ads.s5.pdm.agenda.adapter.OnContatoClickListener
import br.edu.ifsp.scl.ads.s5.pdm.agenda.controller.ContatoController
import br.edu.ifsp.scl.ads.s5.pdm.agenda.databinding.ActivityMainBinding
import br.edu.ifsp.scl.ads.s5.pdm.agenda.model.Contato
import br.edu.ifsp.scl.ads.s5.pdm.agenda.view.MainActivity.Extras.EXTRA_CONTATO
import br.edu.ifsp.scl.ads.s5.pdm.agenda.view.MainActivity.Extras.VISUALIZAR_CONTATO_ACTION

class MainActivity : AppCompatActivity(), OnContatoClickListener {
    // Data source do Adapter
    private lateinit var contatosList: MutableList<Contato>
    // Adapter do RecyclerView
    private lateinit var contatosAdapter: ContatosAdapter
    // LayoutManager do RecyclerView
    private lateinit var contatosLayoutManager: LinearLayoutManager

    // Classe ViewBinding para evitar chamadas a findViewById
    private lateinit var activityMainBinding: ActivityMainBinding

    // Constantes para a ContatoActivity
    private val NOVO_CONTATO_REQUEST_CODE = 0
    private val EDITAR_CONTATO_REQUEST_CODE = 1
    object Extras {
        val EXTRA_CONTATO = "EXTRA_CONTATO"
        val VISUALIZAR_CONTATO_ACTION = "VISUALIZAR_CONTATO_ACTION"
    }

    // Controller
    private lateinit var contatoController: ContatoController

    override fun onStart() {
        super.onStart()

        val email = AutenticadorFirebase.firebaseAuth.currentUser?.email
        if (email == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instancia classe de ViewBinding e seta layout como sendo o root
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // Instanciando Controller
        contatoController = ContatoController(this)

        // Inicializando lista de contatos para o Adapter
        contatosList = mutableListOf()
        val populaContatosListAt = object: AsyncTask<Void, Void, List<Contato>>() {
            override fun doInBackground(vararg p0: Void?): List<Contato> {
                // Thread filha
                Thread.sleep(5000)
                return contatoController.buscaContatos()
            }

            override fun onPreExecute() {
                super.onPreExecute()
                // Thread de GUI
                activityMainBinding.contatosListPb.visibility = View.VISIBLE
                activityMainBinding.listaContatosRv.visibility = View.GONE
            }

            override fun onPostExecute(result: List<Contato>?) {
                super.onPostExecute(result)
                // Thread de GUI
                activityMainBinding.contatosListPb.visibility = View.GONE
                activityMainBinding.listaContatosRv.visibility = View.VISIBLE
                if (result != null) {
                    contatosList.clear()
                    contatosList.addAll(result)
                    contatosAdapter.notifyDataSetChanged()
                }
            }
        }
        populaContatosListAt.execute()
        contatosLayoutManager = LinearLayoutManager(this)
        contatosAdapter = ContatosAdapter(contatosList, this)
        activityMainBinding.listaContatosRv.adapter = contatosAdapter
        activityMainBinding.listaContatosRv.layoutManager = contatosLayoutManager
    }

    override fun onContatoClick(position: Int) {
        val contato: Contato = contatosList[position]

        val visualizarContatoIntent = Intent(this, ContatoActivity::class.java)
        visualizarContatoIntent.putExtra(EXTRA_CONTATO, contato)
        visualizarContatoIntent.action = VISUALIZAR_CONTATO_ACTION

        startActivity(visualizarContatoIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            if (item.itemId == R.id.novoContatoMi) {
                val novoContantoIntent = Intent(this, ContatoActivity::class.java)
                startActivityForResult(novoContantoIntent, NOVO_CONTATO_REQUEST_CODE)
                true
            } else if (item.itemId == R.id.sairMi) {
                AutenticadorFirebase.firebaseAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            else
                false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NOVO_CONTATO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            data.getParcelableExtra<Contato>(EXTRA_CONTATO)?.let { contato ->
                contatoController.insereContato(contato)
                contatosList.add(contato)
                contatosAdapter.notifyDataSetChanged()
            }
        } else {
            if (requestCode == EDITAR_CONTATO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                data.getParcelableExtra<Contato>(EXTRA_CONTATO)?.let { contato ->
                    contatoController.atualizaContato(contato)
                    contatosList[contatosList.indexOfFirst { it.nome == contato.nome }] = contato
                    contatosAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onEditarMenuItemClick(position: Int) {
        val contatoSelecionado: Contato = contatosList[position]

        val editarContatoIntent = Intent(this, ContatoActivity::class.java)
        editarContatoIntent.putExtra(EXTRA_CONTATO, contatoSelecionado)
        startActivityForResult(editarContatoIntent, EDITAR_CONTATO_REQUEST_CODE)
    }

    override fun onRemoverMenuItemClick(position: Int) {
        val contatoExcluido = contatosList[position]

        if (position != -1) {
            contatoController.removeContato(contatoExcluido.nome)
            contatosList.removeAt(position)
            contatosAdapter.notifyDataSetChanged()
        }
    }
}
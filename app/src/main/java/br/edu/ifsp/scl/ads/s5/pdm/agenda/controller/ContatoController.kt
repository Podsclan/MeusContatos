package br.edu.ifsp.scl.ads.s5.pdm.agenda.controller

import br.edu.ifsp.scl.ads.s5.pdm.agenda.model.Contato
import br.edu.ifsp.scl.ads.s5.pdm.agenda.model.ContatoDao
import br.edu.ifsp.scl.ads.s5.pdm.agenda.model.ContatoFirebase
import br.edu.ifsp.scl.ads.s5.pdm.agenda.model.ContatoSqlite
import br.edu.ifsp.scl.ads.s5.pdm.agenda.view.MainActivity

class ContatoController(mainActivity: MainActivity) {
    val contatoDao: ContatoDao
    init {
        contatoDao = ContatoFirebase()
    }

    fun insereContato(contato: Contato) = contatoDao.createContato(contato)
    fun buscaContatos() = contatoDao.readContatos()
    fun atualizaContato(contato: Contato) = contatoDao.updateContato(contato)
    fun removeContato(nome: String) = contatoDao.deleteContato(nome)
}
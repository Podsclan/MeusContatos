package br.edu.ifsp.scl.ads.s5.pdm.agenda.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contato (
    val nome: String = "",
    val userId: String = "",
    var telefone: String = "",
    var email: String = ""
): Parcelable
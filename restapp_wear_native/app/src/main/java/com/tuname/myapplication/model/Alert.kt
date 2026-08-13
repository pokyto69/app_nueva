package com.tuname.myapplication.model

import com.google.firebase.Timestamp

data class Alert(
    val id: String = "",
    val mesaId: String = "",
    val mesaNumero: Int = 0,
    val tipo: String = "", // LLAMAR, PAGO
    val atendida: Boolean = false,
    val timestamp: Timestamp = Timestamp.now()
)

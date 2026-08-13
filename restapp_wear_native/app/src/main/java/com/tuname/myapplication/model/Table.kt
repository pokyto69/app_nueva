package com.tuname.myapplication.model

import com.google.firebase.Timestamp

data class Table(
    val id: String = "",
    val numero: Int = 0,
    val status: String = "libre", // libre, ocupada, pago, reservada
    val orden: List<OrderItem> = emptyList(),
    val solicitudPago: PaymentRequest? = null,
    val totalCobrar: Double? = null,
    val atendidoPor: String? = null
)

data class PaymentRequest(
    val solicitadoPor: String = "",
    val timestamp: Timestamp? = null
)

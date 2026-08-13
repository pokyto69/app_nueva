package com.tuname.myapplication.model

data class OrderItem(
    val nombre: String = "",
    val precio: Double = 0.0,
    val cantidad: Int = 1,
    val platilloId: String = ""
)

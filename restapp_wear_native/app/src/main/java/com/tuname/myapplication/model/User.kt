package com.tuname.myapplication.model

data class User(
    val id: String = "", // Map to 'id' field in Firestore document
    val nombre: String = "",
    val rol: String = "mesero",
    val pin: String = ""
)

package com.example.aplicacioncrud
data class LoginResponse(
    val success: Boolean,
    val mensaje: String,
    val nombre: String?,
    val apellido: String?
)
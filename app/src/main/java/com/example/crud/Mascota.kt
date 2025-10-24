package com.example.crud.modelos

// Una "data class" es perfecta para guardar datos.
// No necesita id aquí, lo manejaremos en la base de datos y al leer.
data class Mascota(
    val id: Int,
    val nombre: String,
    val edad: Int
)

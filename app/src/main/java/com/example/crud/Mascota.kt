// Archivo: app/src/main/java/com/example/crud/modelos/Mascota.kt

package com.example.crud.modelos

data class Mascota(
    var nombre: String,
    var edad: Int,
    var id: Long = 0
)

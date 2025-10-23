// 1. PAQUETE CORRECTO
package com.example.crud

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.crud.AyudanteBaseDeDatos.Companion.COLUMNA_EDAD
import com.example.crud.AyudanteBaseDeDatos.Companion.COLUMNA_ID
import com.example.crud.AyudanteBaseDeDatos.Companion.COLUMNA_NOMBRE
import com.example.crud.AyudanteBaseDeDatos.Companion.NOMBRE_TABLA_MASCOTAS

// 2. IMPORTACIÓN DE TU CLASE JAVA
import com.example.crud.R
import com.example.crud.modelos.Mascota

// El nombre de la clase puede ser el que quieras. "Controlador" es una buena opción.
class Controlador(contexto: Context) {
    private val ayudanteBaseDeDatos: AyudanteBaseDeDatos = AyudanteBaseDeDatos(contexto)

    fun eliminarMascota(mascota: Mascota): Int {
        val baseDeDatos: SQLiteDatabase = ayudanteBaseDeDatos.writableDatabase
        val argumentos = arrayOf(mascota.id.toString())
        return baseDeDatos.delete(NOMBRE_TABLA_MASCOTAS, "$COLUMNA_ID = ?", argumentos)
    }

    fun nuevaMascota(mascota: Mascota): Long {
        val baseDeDatos: SQLiteDatabase = ayudanteBaseDeDatos.writableDatabase
        val valoresParaInsertar = ContentValues().apply {
            // Usa los métodos get() de tu clase Java
            put(COLUMNA_NOMBRE, mascota.nombre)
            put(COLUMNA_EDAD, mascota.edad)
        }
        return baseDeDatos.insert(NOMBRE_TABLA_MASCOTAS, null, valoresParaInsertar)
    }

    fun guardarCambios(mascotaEditada: Mascota): Int {
        val baseDeDatos: SQLiteDatabase = ayudanteBaseDeDatos.writableDatabase
        val valoresParaActualizar = ContentValues().apply {
            put(COLUMNA_NOMBRE, mascotaEditada.nombre)
            put(COLUMNA_EDAD, mascotaEditada.edad)
        }
        val campoParaActualizar = "$COLUMNA_ID = ?"
        val argumentosParaActualizar = arrayOf(mascotaEditada.id.toString())
        return baseDeDatos.update(
            NOMBRE_TABLA_MASCOTAS,
            valoresParaActualizar,
            campoParaActualizar,
            argumentosParaActualizar
        )
    }

    fun obtenerMascotas(): ArrayList<Mascota> {
        val mascotas = ArrayList<Mascota>()
        val baseDeDatos: SQLiteDatabase = ayudanteBaseDeDatos.readableDatabase
        val columnasAConsultar = arrayOf(COLUMNA_ID, COLUMNA_NOMBRE, COLUMNA_EDAD)

        // El bloque 'use' asegura que el cursor se cierre automáticamente
        baseDeDatos.query(
            NOMBRE_TABLA_MASCOTAS,
            columnasAConsultar,
            null, null, null, null, null
        )?.use { cursor -> // El ?.use se ejecuta solo si el cursor no es nulo
            if (cursor.moveToFirst()) {
                do {
                    val idIndex = cursor.getColumnIndex(COLUMNA_ID)
                    val nombreIndex = cursor.getColumnIndex(COLUMNA_NOMBRE)
                    val edadIndex = cursor.getColumnIndex(COLUMNA_EDAD)

                    if (idIndex == -1 || nombreIndex == -1 || edadIndex == -1) continue

                    val idMascota = cursor.getLong(idIndex)
                    val nombreObtenido = cursor.getString(nombreIndex)
                    val edadObtenida = cursor.getInt(edadIndex)

                    val mascota = Mascota(nombreObtenido, edadObtenida, idMascota)
                    mascotas.add(mascota)
                } while (cursor.moveToNext())
            }
        }
        return mascotas
    }
}

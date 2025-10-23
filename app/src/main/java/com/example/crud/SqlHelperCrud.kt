// Tu paquete actual es com.example.crud, este es el correcto.
package com.example.crud

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AyudanteBaseDeDatos(context: Context?) :
    SQLiteOpenHelper(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS) {

    companion object {
        private const val NOMBRE_BASE_DE_DATOS = "CrudDB.db"

        // Puedes mantener la versión en 1 si esta es la estructura inicial que quieres.
        private const val VERSION_BASE_DE_DATOS = 1

        // --- NOMBRE DE LA TABLA Y SUS COLUMNAS (VERSIÓN SIMPLE) ---
        const val NOMBRE_TABLA_MASCOTAS = "mascotas"
        const val COLUMNA_ID = "id"
        const val COLUMNA_NOMBRE = "nombre"
        const val COLUMNA_EDAD = "edad"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Creamos la tabla solo con id, nombre y edad.
        val sqlCrearTabla = "CREATE TABLE IF NOT EXISTS $NOMBRE_TABLA_MASCOTAS (" +
                "$COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMNA_NOMBRE TEXT NOT NULL, " +
                "$COLUMNA_EDAD INTEGER NOT NULL)"
        db.execSQL(sqlCrearTabla)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Esta estrategia simple elimina la tabla vieja y crea la nueva.
        db.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA_MASCOTAS")
        onCreate(db)
    }
}

package com.example.crud.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "Mascotas.db", null, 1) {

    // Nombres de la tabla y columnas para evitar errores de tipeo
    companion object {
        const val TABLE_NAME = "mascotas"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_EDAD = "edad"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Query para crear la tabla
        val createTableSQL = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NOMBRE TEXT, " +
                "$COLUMN_EDAD INTEGER)"
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Si actualizas la base de datos (ej. a versión 2), aquí se maneja.
        // Por ahora, simplemente la borramos y la volvemos a crear.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}

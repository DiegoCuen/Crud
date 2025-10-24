package com.example.crud

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.db.DatabaseHelper
import com.example.crud.modelos.Mascota

class MainActivity : AppCompatActivity() {

    // Variables para vistas y base de datos
    private lateinit var etNombre: EditText
    private lateinit var etEdad: EditText
    private lateinit var btnGuardar: Button
    private lateinit var rvMascotas: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: MascotasAdapter
    private var idMascotaSeleccionada: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar DB Helper
        dbHelper = DatabaseHelper(this)

        // Enlazar vistas del layout
        etNombre = findViewById(R.id.etNombre)
        etEdad = findViewById(R.id.etEdad)
        btnGuardar = findViewById(R.id.btnGuardar)
        rvMascotas = findViewById(R.id.rvMascotas)

        // Configurar RecyclerView
        rvMascotas.layoutManager = LinearLayoutManager(this)
        adapter = MascotasAdapter(
            mutableListOf(),
            onEditar = { mascota -> prepararEdicion(mascota) },
            onEliminar = { mascota -> eliminarMascota(mascota.id) }
        )
        rvMascotas.adapter = adapter

        // Configurar el click del botón principal
        btnGuardar.setOnClickListener {
            guardarMascota()
        }

        // Cargar las mascotas existentes al iniciar
        cargarMascotas()
    }

    private fun guardarMascota() {
        val nombre = etNombre.text.toString()
        val edadStr = etEdad.text.toString()

        if (nombre.isBlank() || edadStr.isBlank()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = edadStr.toInt()
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NOMBRE, nombre)
            put(DatabaseHelper.COLUMN_EDAD, edad)
        }

        if (idMascotaSeleccionada == null) {
            // --- CREATE (Crear) ---
            db.insert(DatabaseHelper.TABLE_NAME, null, values)
            Toast.makeText(this, "Mascota guardada", Toast.LENGTH_SHORT).show()
        } else {
            // --- UPDATE (Actualizar) ---
            db.update(DatabaseHelper.TABLE_NAME, values, "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(idMascotaSeleccionada.toString()))
            Toast.makeText(this, "Mascota actualizada", Toast.LENGTH_SHORT).show()
        }

        limpiarFormulario()
        cargarMascotas()
    }

    @SuppressLint("Range")
    private fun cargarMascotas() {
        // --- READ (Leer) ---
        val mascotas = mutableListOf<Mascota>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, "${DatabaseHelper.COLUMN_ID} DESC")

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))
            val nombre = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOMBRE))
            val edad = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_EDAD))
            mascotas.add(Mascota(id, nombre, edad))
        }
        cursor.close()
        adapter.actualizarLista(mascotas)
    }

    private fun eliminarMascota(id: Int) {
        // --- DELETE (Eliminar) ---
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_NAME, "${DatabaseHelper.COLUMN_ID} = ?", arrayOf(id.toString()))
        Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show()
        cargarMascotas()
    }

    private fun prepararEdicion(mascota: Mascota) {
        // Rellena el formulario con los datos de la mascota a editar
        idMascotaSeleccionada = mascota.id
        etNombre.setText(mascota.nombre)
        etEdad.setText(mascota.edad.toString())
        btnGuardar.text = "Actualizar"
    }

    private fun limpiarFormulario() {
        etNombre.text.clear()
        etEdad.text.clear()
        idMascotaSeleccionada = null
        btnGuardar.text = "Guardar"
        etNombre.requestFocus()
    }
}

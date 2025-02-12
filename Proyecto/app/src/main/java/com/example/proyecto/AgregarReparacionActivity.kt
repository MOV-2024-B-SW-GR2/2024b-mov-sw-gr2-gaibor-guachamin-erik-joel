package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AgregarReparacionActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etRepuesto: EditText // Cambiar Spinner por EditText
    private var selectedRepuesto: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_reparacion)

        dbHelper = DatabaseHelper(this)
        val bicicletaId = intent.getIntExtra("bicicleta_id", -1)

        // Inicializamos el EditText para ingresar el repuesto
        etRepuesto = findViewById(R.id.et_repuesto) // Asegúrate de tener un EditText en tu layout con este id

        // Configuración del botón Guardar
        val btnGuardar = findViewById<ImageButton>(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            val fecha = findViewById<EditText>(R.id.etFecha).text.toString()
            val tipo = findViewById<EditText>(R.id.etTipo).text.toString()
            val precio = findViewById<EditText>(R.id.etPrecio).text.toString().toDoubleOrNull() ?: 0.0
            val observacion = findViewById<EditText>(R.id.etObservacion).text.toString()

            selectedRepuesto = etRepuesto.text.toString() // Obtenemos el repuesto ingresado en el EditText

            if (fecha.isEmpty() || tipo.isEmpty() || selectedRepuesto.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reparacion = Reparacion(
                bicicletaId = bicicletaId,
                fecha = fecha,
                tipo = tipo,
                repuestos = selectedRepuesto, // Usamos el repuesto ingresado
                precio = precio,
                observacion = observacion
            )

            dbHelper.agregarReparacion(reparacion)
            Toast.makeText(this, "Reparación añadida con éxito", Toast.LENGTH_SHORT).show()
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

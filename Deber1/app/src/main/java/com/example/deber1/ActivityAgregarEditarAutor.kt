package com.example.deber1

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber1.modelos.Autor

class ActivityAgregarEditarAutor : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etNacionalidad: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var cbActivo: CheckBox
    private lateinit var btnGuardar: Button
    private var autorId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_editar_autor)

        etNombre = findViewById(R.id.nombre_input)
        etNacionalidad = findViewById(R.id.nacionalidad_input)
        cbActivo = findViewById(R.id.activo_input)
        btnGuardar = findViewById(R.id.btnGuardar)
        etFechaNacimiento = findViewById(R.id.fechaN_input)

        // Obtener ID del autor a editar
        autorId = intent.getIntExtra("idAutor", -1)

        if (autorId != -1) {
            // Si se pasa un ID válido, cargar los datos del autor
            val autor = BDmemoria.autores.find { it.id == autorId }
            autor?.let {
                etNombre.setText(it.nombre)
                etNacionalidad.setText(it.nacionalidad)
                etFechaNacimiento.setText(it.fechaNacimiento)
                cbActivo.isChecked = it.activo
            }
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val nacionalidad = etNacionalidad.text.toString().trim()
            val fechaNacimiento = etFechaNacimiento.text.toString().trim()
            val activo = cbActivo.isChecked

            // Verificar que los campos no estén vacíos
            if (nombre.isEmpty() || nacionalidad.isEmpty() || fechaNacimiento.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                if (autorId != -1) {
                    // Actualizar los datos del autor
                    val autor = BDmemoria.autores.find { it.id == autorId }
                    autor?.apply {
                        this.nombre = nombre
                        this.nacionalidad = nacionalidad
                        this.fechaNacimiento = fechaNacimiento
                        this.activo = activo
                    }
                    Toast.makeText(this, "Autor actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    // Crear el nuevo autor
                    val nuevoAutor = Autor(
                        id = BDmemoria.autores.size + 1,
                        nombre = nombre,
                        nacionalidad = nacionalidad,
                        fechaNacimiento = fechaNacimiento,
                        activo = activo,
                        libros = mutableListOf()
                    )
                    // Agregar a la base de datos en memoria
                    BDmemoria.autores.add(nuevoAutor)
                    Toast.makeText(this, "Autor agregado exitosamente", Toast.LENGTH_SHORT).show()
                }

                // Devolver resultado OK a MainActivity
                setResult(RESULT_OK)
                finish()  // Regresar a la pantalla principal
            }
        }
    }
}

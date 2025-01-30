package com.example.deber1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber1.modelos.Libro

class ActivityAgregarEditarLibro : AppCompatActivity() {
    private lateinit var etTitulo: EditText
    private lateinit var etGenero: EditText
    private lateinit var etPrecio: EditText
    private lateinit var btnGuardarLibro: Button

    private var libroId: Int? = null
    private var autorId: Int = 0  // ID del autor al que pertenece el libro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_editar_libro)

        etTitulo = findViewById(R.id.titulo_input)
        etGenero = findViewById(R.id.genero_input)
        etPrecio = findViewById(R.id.precio_input)
        btnGuardarLibro = findViewById(R.id.btnGuardar)

        // Obtener el ID del autor y el ID del libro desde el intent
        autorId = intent.getIntExtra("idAutor", 0)
        libroId = intent.getIntExtra("idLibro", -1).takeIf { it != -1 }

        if (libroId != null) {
            // Si se pasa un ID válido, cargar los datos del libro
            val libro = buscarLibroPorId(libroId)
            libro?.let {
                etTitulo.setText(it.titulo)
                etGenero.setText(it.genero)
                etPrecio.setText(it.precio.toString())
            }
        }

        // Lógica para guardar un nuevo libro o actualizar uno existente
        btnGuardarLibro.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val genero = etGenero.text.toString().trim()
            val precio = etPrecio.text.toString().trim()

            // Verificar que los campos no estén vacíos
            if (titulo.isEmpty() || genero.isEmpty() || precio.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                if (libroId != null) {
                    // Actualizar los datos del libro
                    val libroActualizado = Libro(libroId!!, titulo, genero, precio.toDouble(), autorId)
                    actualizarLibro(libroActualizado)
                    Toast.makeText(this, "Libro actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    // Crear el nuevo libro
                    val nuevoLibro = Libro(
                        id = BDmemoria.autores.flatMap { it.libros }.size + 1,
                        titulo = titulo,
                        genero = genero,
                        precio = precio.toDouble(),
                        autor_id = autorId
                    )
                    // Obtener el autor y agregar el libro
                    val autor = BDmemoria.autores.find { it.id == autorId }
                    autor?.libros?.add(nuevoLibro)
                    Toast.makeText(this, "Libro agregado exitosamente", Toast.LENGTH_SHORT).show()
                }

                // Devolver resultado OK a la actividad anterior
                setResult(RESULT_OK)
                finish()  // Finaliza la actividad
            }
        }
    }

    // Buscar un libro por ID en la lista de libros del autor
    private fun buscarLibroPorId(id: Int?): Libro? {
        for (autor in BDmemoria.autores) {
            val libro = autor.libros.find { it.id == id }
            if (libro != null) return libro
        }
        return null
    }

    // Actualizar el libro en la base de datos
    private fun actualizarLibro(libro: Libro) {
        for (autor in BDmemoria.autores) {
            val index = autor.libros.indexOfFirst { it.id == libro.id }
            if (index != -1) {
                autor.libros[index] = libro
                break
            }
        }
    }
}

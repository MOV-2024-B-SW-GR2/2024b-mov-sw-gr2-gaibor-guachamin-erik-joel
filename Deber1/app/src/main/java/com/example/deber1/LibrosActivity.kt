package com.example.deber1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deber1.controlador.LibrosAdapter
import com.example.deber1.modelos.Libro

class LibrosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAgregarLibro: Button
    private val libros = mutableListOf<Libro>()  // Lista de libros del autor
    private var autorId: Int = 0  // ID del autor al que pertenecen los libros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libros)

        recyclerView = findViewById(R.id.recyclerViewLibros)
        btnAgregarLibro = findViewById(R.id.btnAgregarLibro)

        // Obtener el ID del autor desde el intent
        autorId = intent.getIntExtra("idAutor", 0)

        // Cargar los libros del autor
        cargarLibros()

        // Configuración del adapter para el RecyclerView
        val adapter = LibrosAdapter(
            libros,
            onEdit = { libro ->
                // Lógica para editar el libro
                val intent = Intent(this, ActivityAgregarEditarLibro::class.java)
                intent.putExtra("idLibro", libro.id)
                startActivityForResult(intent, EDITAR_LIBRO_REQUEST)
            },
            onDelete = { id ->
                // Eliminar libro y actualizar la lista
                libros.removeAll { it.id == id }
                recyclerView.adapter?.notifyDataSetChanged()
            }
        )

        // Configurar el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Manejo de la acción de agregar un libro
        btnAgregarLibro.setOnClickListener {
            val intent = Intent(this, ActivityAgregarEditarLibro::class.java)
            intent.putExtra("idAutor", autorId)
            startActivityForResult(intent, AGREGAR_LIBRO_REQUEST)
        }
    }

    // Este método es necesario para recibir el resultado de la actividad de agregar/editar libro
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AGREGAR_LIBRO_REQUEST && resultCode == RESULT_OK) {
            // Si la actividad de agregar libro se completó correctamente, actualizar la lista
            cargarLibros()
            recyclerView.adapter?.notifyDataSetChanged()
        } else if (requestCode == EDITAR_LIBRO_REQUEST && resultCode == RESULT_OK) {
            // Si la actividad de editar libro se completó correctamente, actualizar la lista
            cargarLibros()
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    // Este método carga los libros del autor seleccionado
    private fun cargarLibros() {
        // Aquí cargamos la lista de libros del autor específico
        val autor = BDmemoria.autores.find { it.id == autorId }
        libros.clear()
        autor?.libros?.let {
            libros.addAll(it)
        }
    }

    companion object {
        const val AGREGAR_LIBRO_REQUEST = 1
        const val EDITAR_LIBRO_REQUEST = 2
    }
}

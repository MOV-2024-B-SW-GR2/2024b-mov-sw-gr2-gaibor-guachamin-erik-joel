package com.example.deber1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deber1.LibrosActivity.Companion.EDITAR_LIBRO_REQUEST
import com.example.deber1.controlador.AutoresAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAgregarAutor: Button
    private val autores = BDmemoria.autores

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewAutores)
        btnAgregarAutor = findViewById(R.id.btnAgregarAutor)

        // Configuración del adapter para el RecyclerView
        val adapter = AutoresAdapter(
            autores,
            onClick = { autor ->
                val intent = Intent(this, LibrosActivity::class.java)
                intent.putExtra("idAutor", autor.id)
                startActivityForResult(intent, EDITAR_LIBRO_REQUEST)
            },
            onEdit = { autor ->
                // Lógica para editar el autor
                val intent = Intent(this, ActivityAgregarEditarAutor::class.java)
                intent.putExtra("idAutor", autor.id)
                startActivityForResult(intent, EDITAR_AUTOR_REQUEST)
            },
            onDelete = { id ->
                // Eliminar autor y actualizar el RecyclerView
                autores.removeAll { it.id == id }
                recyclerView.adapter?.notifyDataSetChanged()
            }
        )

        // Configurar el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Manejo de la acción de agregar un autor
        btnAgregarAutor.setOnClickListener {
            val intent = Intent(this, ActivityAgregarEditarAutor::class.java)
            startActivityForResult(intent, AGREGAR_AUTOR_REQUEST)
        }
    }

    // Este método es necesario para recibir el resultado de la actividad de edición
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDITAR_AUTOR_REQUEST && resultCode == RESULT_OK) {
            // Si la actividad de edición se completó correctamente, actualizar la lista
            recyclerView.adapter?.notifyDataSetChanged()
        } else if (requestCode == AGREGAR_AUTOR_REQUEST && resultCode == RESULT_OK) {
            // Si la actividad de agregar autor se completó correctamente, actualizar la lista
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        const val EDITAR_AUTOR_REQUEST = 1
        const val AGREGAR_AUTOR_REQUEST = 2
    }
}

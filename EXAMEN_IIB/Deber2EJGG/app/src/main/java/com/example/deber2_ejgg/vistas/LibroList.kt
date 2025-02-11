package com.example.deber2_ejgg.vistas

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.deber2_ejgg.R
import com.example.deber2_ejgg.controlador.ControladorAutor
import com.example.deber2_ejgg.controlador.ControladorLibro
import com.example.deber2_ejgg.entidad.Libro

class LibroList : AppCompatActivity() {
    private lateinit var controladorLibro: ControladorLibro
    private lateinit var controladorAutor: ControladorAutor
    private lateinit var tv_nombre_autor: TextView
    private lateinit var lv_libros: ListView
    private lateinit var btn_agregar_libro: Button
    private var idAutor: Int = 0
    private var libroSeleccionado: Libro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_libro_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        controladorAutor = ControladorAutor(this)
        controladorLibro = ControladorLibro(this)

        tv_nombre_autor = findViewById(R.id.tv_titulo_libro)
        lv_libros = findViewById(R.id.lv_libros)
        btn_agregar_libro = findViewById(R.id.btn_agregar_libro)

        idAutor = intent.getIntExtra("idAutor", 0)

        val autor = controladorAutor.listarAutores().find { it.id == idAutor }
        if (autor != null) {
            tv_nombre_autor.text = autor.nombre
        } else {
            tv_nombre_autor.text = "Autor no encontrado"
        }

        btn_agregar_libro.setOnClickListener {
            val intent = Intent(this, LibroDetalle::class.java)
            intent.putExtra("idAutor", idAutor)
            startActivity(intent)
        }

        registerForContextMenu(lv_libros)
        actualizarLista()
    }

    private fun actualizarLista() {
        val libros = controladorLibro.listarLibrosPorAutor(idAutor)
        if (libros.isEmpty()) {
            Toast.makeText(this, "No hay libros para este autor", Toast.LENGTH_SHORT).show()
        }
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, libros.map { it.titulo })
        lv_libros.adapter = adapter

        lv_libros.setOnItemLongClickListener { _, _, position, _ ->
            libroSeleccionado = libros[position]
            false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_libro, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.im_editar_libro -> {
                val intent = Intent(this, LibroDetalle::class.java)
                intent.putExtra("idAutor", idAutor)
                intent.putExtra("idLibro", libroSeleccionado?.id)
                startActivity(intent)
            }

            R.id.im_eliminar_libro -> {
                if (libroSeleccionado != null) {
                    controladorLibro.eliminarLibro(libroSeleccionado!!.id)
                    Toast.makeText(this, "Libro eliminado correctamente.", Toast.LENGTH_SHORT).show()
                    actualizarLista()
                }
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        controladorLibro.depurarLibros()
        actualizarLista()
    }
}

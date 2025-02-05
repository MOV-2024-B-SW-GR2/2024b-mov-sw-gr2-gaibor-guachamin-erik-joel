package com.example.deber2_ejgg.vistas

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.deber2_ejgg.R
import com.example.deber2_ejgg.controlador.ControladorAutor
import com.example.deber2_ejgg.entidad.Autor

class AutorList : AppCompatActivity() {
    private lateinit var controladorAutor: ControladorAutor
    private lateinit var lv_autores: ListView
    private lateinit var btn_agregar_autor: Button
    private var autorSeleccionado: Autor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_autor_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        controladorAutor = ControladorAutor(this)


        lv_autores = findViewById(R.id.lv_autores)
        btn_agregar_autor = findViewById(R.id.btn_agregar_autor)

        btn_agregar_autor.setOnClickListener {
            val intent = Intent(this, AutorDetalle::class.java)
            startActivity(intent)
        }

        registerForContextMenu(lv_autores)
        actualizarLista()
    }




    private fun actualizarLista() {
        val autores = controladorAutor.listarAutores()

        if (autores.isNotEmpty()) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, autores.map { it.nombre })
            lv_autores.adapter = adapter
        }
        lv_autores.setOnItemClickListener { _, _, position, _ ->
            autorSeleccionado = autores[position]
            lv_autores.showContextMenu()
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_autor, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.im_listar_libros -> {
                val intent = Intent(this, LibroList::class.java)
                intent.putExtra("idAutor", autorSeleccionado?.id)
                startActivity(intent)
            }

            R.id.im_editar_autor -> {
                val intent = Intent(this, AutorDetalle::class.java)
                intent.putExtra("idAutor", autorSeleccionado?.id)
                startActivity(intent)
            }

            R.id.im_eliminar_autor -> {
                if (autorSeleccionado != null) {
                    controladorAutor.eliminarAutor(autorSeleccionado!!.id)
                    Toast.makeText(this, "Autor eliminado correctamente.", Toast.LENGTH_SHORT).show()
                    actualizarLista()
                }
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        actualizarLista()
    }
}

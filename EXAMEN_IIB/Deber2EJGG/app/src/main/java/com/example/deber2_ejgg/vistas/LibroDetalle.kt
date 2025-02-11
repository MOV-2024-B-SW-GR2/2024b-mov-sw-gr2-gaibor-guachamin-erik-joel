package com.example.deber2_ejgg.vistas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.deber2_ejgg.R
import com.example.deber2_ejgg.controlador.ControladorLibro
import com.example.deber2_ejgg.entidad.Libro

class LibroDetalle : AppCompatActivity() {
    private lateinit var controladorLibro: ControladorLibro
    private lateinit var tv_titulo_libro: EditText
    private lateinit var tv_genero_libro: EditText
    private lateinit var tv_precio_libro: EditText
    private lateinit var btn_guardar_libro: Button
    private var idAutor: Int = 0
    private var idLibro: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_libro_detalle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        controladorLibro = ControladorLibro(this)
        tv_titulo_libro = findViewById(R.id.tv_titulo_libro)
        tv_genero_libro = findViewById(R.id.tv_genero)
        tv_precio_libro = findViewById(R.id.tv_precio)
        btn_guardar_libro = findViewById(R.id.btn_guardar_libro)
        val tv_formulario_libro: TextView = findViewById(R.id.tv_formulario_libro)

        idAutor = intent.getIntExtra("idAutor", 0)
        idLibro = intent.getIntExtra("idLibro", 0).takeIf { it != 0 }

        tv_formulario_libro.text = if (idLibro != null) "Editar libro" else "Agregar libro"

        if (idLibro != null) {
            val libro = controladorLibro.listarLibrosPorAutor(idAutor)
                .find { it.id == idLibro }
            libro?.let {
                tv_titulo_libro.setText(it.titulo)
                tv_genero_libro.setText(it.genero)
                tv_precio_libro.setText(it.precio.toString())
            }
        }

        btn_guardar_libro.setOnClickListener {
            guardarLibro()
        }
    }

    private fun guardarLibro() {
        val titulo = tv_titulo_libro.text.toString()
        val genero = tv_genero_libro.text.toString()
        val precio = tv_precio_libro.text.toString().toDoubleOrNull()

        if (titulo.isNotEmpty() && genero.isNotEmpty() && precio != null) {
            if (idLibro != null) {
                controladorLibro.actualizarLibro(
                    Libro(id = idLibro!!, titulo = titulo, genero = genero, precio = precio, autor_id = idAutor)
                )
                Toast.makeText(this, "Libro actualizado", Toast.LENGTH_SHORT).show()
            } else {
                controladorLibro.crearLibro(
                    idAutor,
                    Libro(id = 0, titulo = titulo, genero = genero, precio = precio, autor_id = idAutor)
                )
                Toast.makeText(this, "Libro creado", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Toast.makeText(this, "Llene todos los campos antes de continuar.", Toast.LENGTH_SHORT).show()
        }
    }
}

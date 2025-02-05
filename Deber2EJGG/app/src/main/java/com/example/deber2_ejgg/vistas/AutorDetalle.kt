package com.example.deber2_ejgg.vistas

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.deber2_ejgg.R
import com.example.deber2_ejgg.controlador.ControladorAutor
import com.example.deber2_ejgg.entidad.Autor

class AutorDetalle : AppCompatActivity() {
    private var idAutor: Int? = null
    private lateinit var controladorAutor: ControladorAutor
    private lateinit var tv_nombre_autor: EditText
    private lateinit var tv_nacionalidad_autor: EditText
    private lateinit var tv_fecha_nacimiento_autor: EditText
    private lateinit var cb_activo_autor: CheckBox
    private lateinit var btn_guardar_autor: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_autor_detalle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        controladorAutor = ControladorAutor(this)

        tv_nombre_autor = findViewById(R.id.tv_nombre)
        tv_nacionalidad_autor = findViewById(R.id.tv_nacionalidad)
        tv_fecha_nacimiento_autor = findViewById(R.id.tv_fNacimiento)
        cb_activo_autor = findViewById(R.id.activo_input)
        btn_guardar_autor = findViewById(R.id.btn_guardar_autor)
        val tv_formulario_autor: TextView = findViewById(R.id.tv_formulario_autor)

        idAutor = intent.getIntExtra("idAutor", -1).takeIf { it != -1 }

        if (idAutor != null) {
            tv_formulario_autor.text = "Editar el Autor"
            val autor = controladorAutor.listarAutores().find { it.id == idAutor }
            autor?.let {
                tv_nombre_autor.setText(it.nombre)
                tv_nacionalidad_autor.setText(it.nacionalidad)
                tv_fecha_nacimiento_autor.setText(it.fechaNacimiento)
                cb_activo_autor.isChecked = it.activo
            }
        } else {
            tv_formulario_autor.text = "Agregar Autor"
        }

        btn_guardar_autor.setOnClickListener {
            val nombre = tv_nombre_autor.text.toString()
            val nacionalidad = tv_nacionalidad_autor.text.toString()
            val fechaNacimiento = tv_fecha_nacimiento_autor.text.toString()
            val activo = cb_activo_autor.isChecked

            if (nombre.isNotEmpty()
                && nacionalidad.isNotEmpty()
                && fechaNacimiento.isNotEmpty()
            ) {
                if (idAutor != null) {
                    controladorAutor.actualizarAutor(
                        Autor(
                            id = idAutor!!,
                            nombre = nombre,
                            nacionalidad = nacionalidad,
                            fechaNacimiento = fechaNacimiento,
                            activo = activo,
                            libros = mutableListOf()
                        )
                    )
                    Toast.makeText(this, "Autor actualizado correctamente", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    controladorAutor.crearAutor(
                        Autor(
                            nombre = nombre,
                            nacionalidad = nacionalidad,
                            fechaNacimiento = fechaNacimiento,
                            activo = activo,
                            libros = mutableListOf()
                        )
                    )
                    Toast.makeText(this, "Autor creado", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Llene todos los campos antes de continuar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

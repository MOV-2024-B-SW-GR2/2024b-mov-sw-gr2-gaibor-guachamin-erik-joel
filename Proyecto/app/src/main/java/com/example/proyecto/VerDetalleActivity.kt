package com.example.proyecto

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VerDetalleActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var bicicleta: Bicicleta? = null  // Usamos nullable para manejar el caso de null
    private val EDIT_REQUEST_CODE = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var reparacionAdapter: ReparacionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_detalle)

        dbHelper = DatabaseHelper(this)

        val bicicletaId = intent.getIntExtra("bicicleta_id", -1)

        if (bicicletaId != -1) {
            bicicleta = dbHelper.obtenerBicicletaPorId(bicicletaId)

            if (bicicleta != null) {
                val imgBicileta: ImageView = findViewById(R.id.imgBicicleta)
                val txtMarca: TextView = findViewById(R.id.txtMarca)
                val txtTitulo: TextView = findViewById(R.id.txtTitulo)
                val txtModelo: TextView = findViewById(R.id.txtModelo)
                val txtNumSerie: TextView = findViewById(R.id.txtNumSerie)
                val txtNumSerie2: TextView = findViewById(R.id.txtNumSerie2)
                val txtAnio: TextView = findViewById(R.id.txtAnio)
                val txtFechaCompra: TextView = findViewById(R.id.txtFechaCompra)
                val btnEditar: ImageButton = findViewById(R.id.btnEditar)
                val btnEliminar: ImageButton = findViewById(R.id.btnEliminar)
                val btnAgregarReparacion: ImageButton = findViewById(R.id.btnAgregarReparacion)
                recyclerView = findViewById(R.id.recyclerReparaciones)

                txtTitulo.text = "${bicicleta?.marca} ${bicicleta?.modelo}"
                txtMarca.text = "Marca: ${bicicleta?.marca}"
                txtModelo.text = "Modelo: ${bicicleta?.modelo}"
                txtNumSerie.text = "${bicicleta?.num_serie}"
                txtNumSerie2.text = "Numero de Serie: ${bicicleta?.num_serie}"
                txtAnio.text = "Año: ${bicicleta?.anio}"
                txtFechaCompra.text = "Fecha de compra: ${bicicleta?.fechaCompra}"

                val imagePath = bicicleta?.imagePath
                if (!imagePath.isNullOrEmpty()) {
                    try {
                        val fileInputStream = openFileInput(imagePath)
                        val bitmap = BitmapFactory.decodeStream(fileInputStream)
                        imgBicileta.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
                    }
                }

                btnEditar.setOnClickListener {
                    val intent = Intent(this, EditBicicletaActivity::class.java)
                    intent.putExtra("bicicleta_id", bicicleta?.id)
                    startActivityForResult(intent, EDIT_REQUEST_CODE)
                }

                btnEliminar.setOnClickListener {
                    mostrarDialogoConfirmacion(bicicletaId)
                }

                btnAgregarReparacion.setOnClickListener {
                    val intent = Intent(this, AgregarReparacionActivity::class.java)
                    intent.putExtra("bicicleta_id", bicicleta?.id)
                    startActivity(intent)
                }

                // Configurar RecyclerView
                recyclerView.layoutManager = LinearLayoutManager(this)
                cargarReparaciones(bicicletaId)
            } else {
                Toast.makeText(this, "Bicicleta no encontrada", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "ID de la Bicicleta no válido", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun cargarReparaciones(bicicletaId: Int) {
        val reparaciones = dbHelper.obtenerReparacionesPorBicicleta(bicicletaId)
        reparacionAdapter = ReparacionAdapter(reparaciones)
        recyclerView.adapter = reparacionAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun mostrarDialogoConfirmacion(bicicletaId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Bicicleta")
            .setMessage("¿Estás seguro de que deseas eliminar este bicicleta?")
            .setPositiveButton("Sí") { _, _ ->
                dbHelper.eliminarBicicleta(bicicletaId)
                Toast.makeText(this, "Bicicleta eliminada", Toast.LENGTH_SHORT).show()
                val intent = Intent()
                setResult(RESULT_OK, intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}


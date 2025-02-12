package com.example.proyecto

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class EditBicicletaActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var imgBicicleta: ImageView
    private var selectedImage: Bitmap? = null
    private var bicicleta: Bicicleta? = null
    private var bicicletaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bicicleta)

        dbHelper = DatabaseHelper(this)
        bicicletaId = intent.getIntExtra("bicicleta_id", -1)

        val spinnerMarcas: Spinner = findViewById(R.id.spinnerMarcas)
        val marcas = listOf("Selecciona una marca", "Trek", "Specialized", "Giant", "Cannondale", "Bianchi", "Scott", "Pinarello", "GT Bicycles", "Felt")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, marcas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMarcas.adapter = adapter

        val editModelo: EditText = findViewById(R.id.editModelo)
        val editNumSerie: EditText = findViewById(R.id.editNumSerie)
        val editAnio: EditText = findViewById(R.id.editAnio)
        val editFechaCompra: EditText = findViewById(R.id.editFechaCompra)
        val txtBtn: Button = findViewById(R.id.btnAgregarBicicleta)

        imgBicicleta = findViewById(R.id.imgBicicleta)

        if (bicicletaId != -1) {
            bicicleta = dbHelper.obtenerBicicletaPorId(bicicletaId)
            bicicleta?.let {
                spinnerMarcas.setSelection(marcas.indexOf(it.marca))
                editModelo.setText(it.modelo)
                editNumSerie.setText(it.num_serie)
                editAnio.setText(it.anio.toString())
                editFechaCompra.setText(it.fechaCompra)
                txtBtn.setText("Actualizar Bicicleta")

                val file = File(filesDir, it.imagePath)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    imgBicicleta.setImageBitmap(bitmap)
                    selectedImage = bitmap
                } else {
                    imgBicicleta.setImageResource(R.drawable.foto)
                }
            }
        }

        imgBicicleta.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        val btnActualizarBicicleta: Button = findViewById(R.id.btnAgregarBicicleta)
        btnActualizarBicicleta.setOnClickListener {
            val marca = spinnerMarcas.selectedItem.toString()
            val modelo = editModelo.text.toString()
            val NumSerie = editNumSerie.text.toString()
            val anio = editAnio.text.toString().toIntOrNull() ?: 0
            val fechaCompra = editFechaCompra.text.toString()

            if (marca == "Selecciona una marca" || modelo.isEmpty() || NumSerie.isEmpty() || anio == 0 || fechaCompra.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Guardar la imagen, si se ha seleccionado una nueva
                val imagePath = selectedImage?.let { saveImageToStorage(it) } ?: bicicleta?.imagePath ?: ""

                // Crear el bicicleta actualizado
                val updatedBicicleta = Bicicleta(
                    id = bicicletaId,
                    marca = marca,
                    modelo = modelo,
                    anio = anio,
                    num_serie = NumSerie,
                    fechaCompra = fechaCompra,
                    imagePath = imagePath
                )

                // Actualizar el bicicleta en la base de datos
                dbHelper.actualizarBicicleta(updatedBicicleta)
                Toast.makeText(this, "Bicicleta actualizado correctamente", Toast.LENGTH_SHORT).show()

                // Configurar el resultado de la actividad como OK
                setResult(RESULT_OK)

                // Finalizar la actividad actual (EditBicicletaActivity)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imgBicicleta.setImageBitmap(bitmap)
                selectedImage = bitmap
            }
        }
    }

    private fun saveImageToStorage(image: Bitmap): String {
        val fileName = "bicicleta_image_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)
        image.compress(Bitmap.CompressFormat.JPEG, 100, file.outputStream())
        return fileName
    }
}

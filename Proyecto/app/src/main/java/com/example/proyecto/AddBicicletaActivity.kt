package com.example.proyecto

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddBicicletaActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var imgBicicleta: ImageView
    private var selectedImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bicicleta) // Asegúrate de que el layout sea correcto

        dbHelper = DatabaseHelper(this)

        // Configuración del Spinner para las marcas
        val spinnerMarcas: Spinner = findViewById(R.id.spinnerMarcas)
        val marcas = listOf("Selecciona una marca", "Trek", "Specialized", "Giant", "Cannondale", "Bianchi", "Scott", "Pinarello", "GT Bicycles", "Felt")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, marcas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMarcas.adapter = adapter

        // Referencias a los EditText para los demás datos
        val editModelo: EditText = findViewById(R.id.editModelo)
        val editNumSerie: EditText = findViewById(R.id.editNumSerie)
        val editAnio: EditText = findViewById(R.id.editAnio)
        val editFechaCompra: EditText = findViewById(R.id.editFechaCompra)

        // Referencia al ImageView para la imagen del bicicleta
        imgBicicleta = findViewById(R.id.imgBicicleta)

        // Botón para seleccionar imagen desde la galería
        imgBicicleta.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100) // Código de solicitud 100
        }

        // Botón para agregar el bicicleta
        val btnAgregarBicicleta: Button = findViewById(R.id.btnAgregarBicicleta)
        btnAgregarBicicleta.setOnClickListener {
            // Obtiene la marca seleccionada del Spinner
            val marca = spinnerMarcas.selectedItem.toString()
            val modelo = editModelo.text.toString()
            val numSerie = editNumSerie.text.toString()
            val anio = editAnio.text.toString().toIntOrNull() ?: 0
            val fechaCompra = editFechaCompra.text.toString()

            // Verifica que ninguno de los campos esté vacío y que se haya seleccionado una imagen
            if (marca == "Selecciona una marca" || modelo.isEmpty() || numSerie.isEmpty() || anio == 0 || fechaCompra.isEmpty() || selectedImage == null) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Guarda la imagen y obtiene su ruta
                val imagePath = saveImageToStorage(selectedImage)
                // Crea el objeto Bicicleta (asegúrate de tener definida la clase Bicicleta con los nuevos campos)
                val bicicleta = Bicicleta(
                    id = 0, // El ID se generará automáticamente en la base de datos
                    marca = marca,
                    modelo = modelo,
                    anio = anio,
                    num_serie = numSerie,
                    fechaCompra = fechaCompra, // Agregar el campo fecha de compra
                    imagePath = imagePath
                )
                // Agrega el bicicleta a la base de datos
                dbHelper.agregarBicicleta(bicicleta)
                Toast.makeText(this, "Bicicleta agregado correctamente", Toast.LENGTH_SHORT).show()
                // Finaliza la actividad y retorna RESULT_OK para actualizar la lista de bicicletas en la actividad anterior
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    // Manejo del resultado de la selección de imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imgBicicleta.setImageBitmap(bitmap)
                selectedImage = bitmap // Guarda la imagen seleccionada
            }
        }
    }

    // Función para guardar la imagen en almacenamiento interno y devolver la ruta relativa
    private fun saveImageToStorage(image: Bitmap?): String {
        val fileName = "bicicleta_image_${System.currentTimeMillis()}.jpg"
        var filePath = ""
        try {
            val fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            image?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
            filePath = fileName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return filePath
    }
}

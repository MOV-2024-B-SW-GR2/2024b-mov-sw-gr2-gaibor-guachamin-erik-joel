package com.example.proyecto

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el helper de base de datos
        dbHelper = DatabaseHelper(this)

        val btnAgregar: ImageButton = findViewById(R.id.btn_add)
        btnAgregar.setOnClickListener {
            mostrarDialogoAgregar()
        }

        // Inicializar y configurar RecyclerViews
        setupBicicletaRecyclerView()
    }

    private fun mostrarDialogoAgregar() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_agregar)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true) // Permite cerrar el diálogo tocando fuera
        dialog.show()

        val btnBicicleta: LinearLayout = dialog.findViewById(R.id.btn_bicicleta)
        btnBicicleta.setOnClickListener {
            val intent = Intent(this, AddBicicletaActivity::class.java)
            startActivityForResult(intent, 1) // Iniciar con startActivityForResult
            dialog.dismiss() // Cerrar el diálogo al hacer clic en el botón
        }
    }

    // Configurar RecyclerView para mostrar los vehículos
    private fun setupBicicletaRecyclerView() {
        val bicicletas = dbHelper.obtenerBicicletas() // Recupera los vehículos desde la BD

        val bicicletasRecyclerView: RecyclerView = findViewById(R.id.recycler_bicicletas)
        bicicletasRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bicicletasRecyclerView.adapter = BicicletaAdapter(bicicletas) { selectedBicicleta ->
            // Verifica si el vehículo tiene un ID válido
            if (selectedBicicleta.id != null) {
                Toast.makeText(this, "Vehículo seleccionado: ${selectedBicicleta.id}", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, VerDetalleActivity::class.java)
                intent.putExtra("bicicleta_id", selectedBicicleta.id) // Pasar el ID del vehículo
                startActivityForResult(intent, 100) // Código 100 para editar vehículo
            } else {
                Toast.makeText(this, "ID del vehículo no válido", Toast.LENGTH_SHORT).show()
            }
        }
    }





    // Método que maneja el resultado de otras actividades
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) { // Código 100 para la edición de vehículos
                Log.d("MainActivity", "Resultado OK recibido de EditBicicletaActivity, actualizando lista de vehículos.")
                refreshBicicletaList() // Actualiza la lista de vehículos
            } else if (requestCode == 1) {
                Log.d("MainActivity", "Resultado OK recibido de AddBicicletaActivity, actualizando lista de vehículos.")
                refreshBicicletaList() // Actualiza la lista de vehículos al agregar uno nuevo
            }
        }
    }

    // Método para actualizar el RecyclerView de vehículos
    private fun refreshBicicletaList() {
        val updatedBicicletas = dbHelper.obtenerBicicletas() // Recupera los vehículos actualizados
        Log.d("MainActivity", "Vehículos actualizados: $updatedBicicletas")
        val bicicletaRecyclerView: RecyclerView = findViewById(R.id.recycler_bicicletas)
        val adapter = bicicletaRecyclerView.adapter as BicicletaAdapter
        adapter.updateBicicletas(updatedBicicletas) // Método en el adaptador para actualizar la lista
        Log.d("MainActivity", "Lista de vehículos actualizada en el adaptador.")
    }
}

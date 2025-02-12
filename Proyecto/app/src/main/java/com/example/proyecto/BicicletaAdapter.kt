package com.example.proyecto

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class BicicletaAdapter(
    private var bicicletas: List<Bicicleta>,
    private val onItemClick: (Bicicleta) -> Unit
) : RecyclerView.Adapter<BicicletaAdapter.BicicletaViewHolder>() {

    // Crear y devolver el ViewHolder para cada ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BicicletaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bicicleta, parent, false)
        return BicicletaViewHolder(view)
    }

    // Vincular los datos de cada vehículo con la vista del ViewHolder
    override fun onBindViewHolder(holder: BicicletaViewHolder, position: Int) {
        val bicicleta = bicicletas[position]
        holder.bind(bicicleta)
        holder.itemView.setOnClickListener { onItemClick(bicicleta) } // Maneja el clic en cada ítem
    }

    // Devuelve el número total de elementos en la lista
    override fun getItemCount(): Int = bicicletas.size

    // Actualiza la lista de vehículos y notifica a la vista que se debe actualizar
    fun updateBicicletas(newBicicletas: List<Bicicleta>) {
        bicicletas = newBicicletas
        notifyDataSetChanged()
    }

    // ViewHolder que maneja cada ítem individual
    inner class BicicletaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val biciImage: ImageView = itemView.findViewById(R.id.bici_image)
        private val biciName: TextView = itemView.findViewById(R.id.bici_name)
        private val biciYear: TextView = itemView.findViewById(R.id.bici_year)

        // Asocia los datos del vehículo con las vistas del ViewHolder
        fun bind(bicicleta: Bicicleta) {
            // Asigna el nombre y año del vehículo a los TextViews
            biciName.text = "${bicicleta.marca} ${bicicleta.modelo}"
            biciYear.text = "Año: ${bicicleta.anio}"

            // Cargar la imagen del vehículo
            val file = File(itemView.context.filesDir, bicicleta.imagePath)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath) // Decodifica la imagen
                biciImage.setImageBitmap(bitmap)
            } else {
                biciImage.setImageResource(R.drawable.foto) // Imagen por defecto si no se encuentra
            }
        }
    }
}

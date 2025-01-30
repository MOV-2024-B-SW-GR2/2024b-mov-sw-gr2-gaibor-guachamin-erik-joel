package com.example.deber1.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deber1.R
import com.example.deber1.modelos.Autor

class AutoresAdapter(
    private val autores: MutableList<Autor>,
    private val onClick: (Autor) -> Unit,
    private val onEdit: (Autor) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<AutoresAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreAutor)
        val tvNacionalidad: TextView = view.findViewById(R.id.tvNacionalidadAutor)
        val btnVer: Button = view.findViewById(R.id.btnVer)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_autor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val autor = autores[position]
        holder.tvNombre.text = autor.nombre
        holder.tvNacionalidad.text = autor.nacionalidad
        holder.btnVer.setOnClickListener { onClick(autor) }
        holder.btnEditar.setOnClickListener { onEdit(autor) }
        holder.btnEliminar.setOnClickListener { onDelete(autor.id) }
    }

    override fun getItemCount(): Int = autores.size
}

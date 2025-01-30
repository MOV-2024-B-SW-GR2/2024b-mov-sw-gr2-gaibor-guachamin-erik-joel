package com.example.deber1.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deber1.R
import com.example.deber1.modelos.Libro

class LibrosAdapter(
    private val libros: MutableList<Libro>,
    private val onEdit: (Libro) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<LibrosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloLibro)
        val tvGenero: TextView = view.findViewById(R.id.tvGeneroLibro)
        val btnEditar: Button = view.findViewById(R.id.btnEditarLibro)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminarLibro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val libro = libros[position]
        holder.tvTitulo.text = libro.titulo
        holder.tvGenero.text = libro.genero
        holder.btnEditar.setOnClickListener { onEdit(libro) }
        holder.btnEliminar.setOnClickListener { onDelete(libro.id) }
    }

    override fun getItemCount(): Int = libros.size
}

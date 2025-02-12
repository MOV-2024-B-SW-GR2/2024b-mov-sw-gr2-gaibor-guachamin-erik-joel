package com.example.proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReparacionAdapter(private val reparaciones: List<Reparacion>) :
    RecyclerView.Adapter<ReparacionAdapter.ReparacionViewHolder>() {

    class ReparacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFecha: TextView = view.findViewById(R.id.txtFechaReparacion)
        val txtTipo: TextView = view.findViewById(R.id.txtTipoReparacion)
        val txtRepuestos: TextView = view.findViewById(R.id.txtRepuestos)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecioReparacion)
        val txtObservacion: TextView = view.findViewById(R.id.txtObservacionReparacion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReparacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reparacion, parent, false)
        return ReparacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReparacionViewHolder, position: Int) {
        val reparacion = reparaciones[position]
        holder.txtFecha.text = "Fecha: ${reparacion.fecha}"
        holder.txtTipo.text = "Tipo: ${reparacion.tipo}"
        holder.txtRepuestos.text = "Repuestos: ${reparacion.repuestos}"
        holder.txtPrecio.text = "Precio: $${reparacion.precio}"
        holder.txtObservacion.text = "Observación: ${reparacion.observacion}"
    }

    override fun getItemCount(): Int {
        return reparaciones.size
    }
}

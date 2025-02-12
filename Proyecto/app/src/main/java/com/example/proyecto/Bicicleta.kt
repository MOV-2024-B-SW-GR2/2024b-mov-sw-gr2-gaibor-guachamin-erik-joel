package com.example.proyecto
data class Bicicleta(
    val id: Int,
    val marca: String,
    val modelo: String,
    val anio: Int,
    val imagePath: String,
    val num_serie: String,  // Nuevo atributo
    val fechaCompra: String  // Nuevo atributo
)
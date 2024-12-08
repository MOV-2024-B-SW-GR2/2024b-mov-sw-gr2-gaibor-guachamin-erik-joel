package org.example.models

data class Libro(
    val id: Int,
    var titulo: String,
    var genero: String,
    var precio: Double,
    var autor: Autor // Relación con el Autor
)

package org.example.models

data class Autor(
    val id: Int,
    var nombre: String,
    var nacionalidad: String,
    var fechaNacimiento: String,
    var activo: Boolean,
    var libros: MutableList<Libro> = mutableListOf() // Relación uno a muchos
)

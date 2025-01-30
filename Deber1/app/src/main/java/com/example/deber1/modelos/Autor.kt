package com.example.deber1.modelos

data class Autor(
    var id: Int = 0,
    var nombre: String,
    var nacionalidad: String?,
    var fechaNacimiento: String?,
    var activo: Boolean = false,
    val libros: MutableList<Libro>
)

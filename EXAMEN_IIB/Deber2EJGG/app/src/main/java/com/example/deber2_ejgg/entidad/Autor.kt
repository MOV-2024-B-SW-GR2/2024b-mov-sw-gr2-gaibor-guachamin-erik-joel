package com.example.deber2_ejgg.entidad

class Autor(
    var id: Int=0,
    var nombre: String,
    var nacionalidad: String?,
    var fechaNacimiento: String?,
    var activo: Boolean = false,
    val libros: MutableList<Libro>,
    var latitud: Double?,
    var longitud: Double?
)

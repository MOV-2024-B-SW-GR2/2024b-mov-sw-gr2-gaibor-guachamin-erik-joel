package org.example.services

import org.example.models.Autor
import java.io.File

class AutorService(private val filePath: String) {

    init {
        verificarYCrearArchivo()
    }

    private fun verificarYCrearArchivo() {
        val file = File(filePath)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
    }

    fun crearAutor(autor: Autor) {
        val nuevoId = generarNuevoId()
        val line = "$nuevoId,${autor.nombre},${autor.nacionalidad},${autor.fechaNacimiento},${autor.activo}"
        File(filePath).appendText("\n"+ line )
    }

    fun listarAutores(): List<Autor> {
        return File(filePath).readLines().filter { it.isNotBlank() }.map { line ->
            val data = line.split(",")
            Autor(
                id = data[0].toInt(),
                nombre = data[1],
                nacionalidad = data[2],
                fechaNacimiento = data[3],
                activo = data[4].toBoolean()
            )
        }
    }

    private fun generarNuevoId(): Int {
        val autores = listarAutores()
        return if (autores.isEmpty()) 1 else autores.maxOf { it.id } + 1
    }

    fun actualizarAutor(id: Int, nombre: String) {
        val autores = listarAutores().toMutableList()
        autores.find { it.id == id }?.nombre = nombre
        guardarAutores(autores)
    }

    fun eliminarAutor(id: Int) {
        val autores = listarAutores().filterNot { it.id == id }
        guardarAutores(autores)
    }

    private fun guardarAutores(autores: List<Autor>) {
        File(filePath).writeText(autores.joinToString("\n") {
            "${it.id},${it.nombre},${it.nacionalidad},${it.fechaNacimiento},${it.activo}"
        })
    }
}

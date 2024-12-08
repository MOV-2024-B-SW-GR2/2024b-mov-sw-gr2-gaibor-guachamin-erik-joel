package org.example.services

import org.example.models.Libro
import java.io.File

class LibroService(private val filePath: String) {

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

    fun crearLibro(libro: Libro) {
        val nuevoId = generarNuevoId()
        val line = "$nuevoId,${libro.titulo},${libro.genero},${libro.precio},${libro.autor.id}"
        File(filePath).appendText("\n"+ line)
    }

    fun listarLibros(autorService: AutorService): List<Libro> {
        return File(filePath).readLines().filter { it.isNotBlank() }.map { line ->
            val data = line.split(",")
            val autorId = data[4].toInt()
            val autor = autorService.listarAutores().find { it.id == autorId }
                ?: throw Exception("Autor con ID $autorId no encontrado.")

            Libro(
                id = data[0].toInt(),
                titulo = data[1],
                genero = data[2],
                precio = data[3].toDouble(),
                autor = autor
            )
        }
    }

    private fun generarNuevoId(): Int {
        val libros = listarLibros(AutorService("src/data/autores.txt"))
        return if (libros.isEmpty()) 1 else libros.maxOf { it.id } + 1
    }

    fun actualizarLibro(id: Int, libro: Libro) {
        val libros = listarLibros(AutorService("src/data/autores.txt")).toMutableList()
        libros.find { it.id == id }?.titulo = libro.titulo
        libros.find { it.id == id }?.precio = libro.precio
        libros.find { it.id == id }?.genero = libro.genero
        libros.find { it.id == id }?.autor = libro.autor
        guardarLibros(libros)
    }

    fun eliminarLibro(id: Int) {
        val libros = listarLibros(AutorService("src/data/autores.txt")).filterNot { it.id == id }
        guardarLibros(libros)
    }

    private fun guardarLibros(libros: List<Libro>) {
        File(filePath).writeText(libros.joinToString("\n") {
            "${it.id},${it.titulo},${it.genero},${it.precio},${it.autor.id}"
        })
    }
}

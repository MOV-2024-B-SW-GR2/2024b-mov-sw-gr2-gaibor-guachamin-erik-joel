package org.example

import org.example.models.Autor
import org.example.models.Libro
import org.example.services.AutorService
import org.example.services.LibroService
import javax.swing.JOptionPane
fun main() {
    val autorService = AutorService("src/data/autores.txt")
    val libroService = LibroService("src/data/libros.txt")

    while (true) {
        val opcion = JOptionPane.showInputDialog(
            null,
            "Seleccione una opción:\n" +
                    "1. Crear Autor\n" +
                    "2. Listar Autores\n" +
                    "3. Actualizar Autor\n" +
                    "4. Eliminar Autor\n" +
                    "5. Crear Libro\n" +
                    "6. Listar Libros\n" +
                    "7. Actualizar Libro\n" +
                    "8. Eliminar Libro\n" +
                    "9. Salir",
            "Gestión de Autores y Libros",
            JOptionPane.INFORMATION_MESSAGE
        )

        when (opcion?.toIntOrNull()) {
            1 -> crearAutor(autorService)
            2 -> listarAutores(autorService)
            3 -> actualizarAutor(autorService)
            4 -> eliminarAutor(autorService) // Llama a la nueva función
            5 -> crearLibroConAutor(libroService, autorService)
            6 -> listarLibrosConAutores(libroService, autorService)
            7 -> actualizarLibro(libroService, autorService)
            8 -> eliminarLibro(libroService, autorService) // Llama a la nueva función
            9 -> return
            else -> JOptionPane.showMessageDialog(null, "Opción no válida", "Error", JOptionPane.ERROR_MESSAGE)
        }
    }
}



fun crearAutor(autorService: AutorService) {
    val nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Autor:")
    val nacionalidad = JOptionPane.showInputDialog("Ingrese la Nacionalidad del Autor:")
    val fechaNacimiento = JOptionPane.showInputDialog("Ingrese la Fecha de Nacimiento del Autor (YYYY-MM-DD):")
    val activo = JOptionPane.showInputDialog("¿Está activo? (true/false):").toBoolean()

    val autor = Autor(0, nombre, nacionalidad, fechaNacimiento, activo) // ID se generará automáticamente
    autorService.crearAutor(autor)
    JOptionPane.showMessageDialog(null, "Autor creado con éxito.")
}

fun listarAutores(autorService: AutorService) {
    val autores = autorService.listarAutores()
    val lista = autores.joinToString("\n") { "ID: ${it.id}, Nombre: ${it.nombre}, Nacionalidad: ${it.nacionalidad}" }
    JOptionPane.showMessageDialog(null, if (autores.isNotEmpty()) lista else "No hay autores registrados.")
}

fun actualizarAutor(autorService: AutorService) {
    val autores = autorService.listarAutores()
    if (autores.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay autores registrados.")
        return
    }

    val listaAutores = autores.joinToString("\n") { "ID: ${it.id}, Nombre: ${it.nombre}" }
    val autorId = JOptionPane.showInputDialog("Seleccione el ID del Autor a actualizar:\n$listaAutores").toInt()
    val autor = autores.find { it.id == autorId }

    if (autor != null) {
        val atributo = JOptionPane.showInputDialog(
            "Seleccione el atributo a actualizar:\n" +
                    "1. Nombre\n" +
                    "2. Nacionalidad\n" +
                    "3. Fecha de Nacimiento\n" +
                    "4. Activo (true/false)"
        ).toInt()

        when (atributo) {
            1 -> {
                val nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo Nombre:")
                autor.nombre = nuevoNombre
            }
            2 -> {
                val nuevaNacionalidad = JOptionPane.showInputDialog("Ingrese la nueva Nacionalidad:")
                autor.nacionalidad = nuevaNacionalidad
            }
            3 -> {
                val nuevaFecha = JOptionPane.showInputDialog("Ingrese la nueva Fecha de Nacimiento (YYYY-MM-DD):")
                autor.fechaNacimiento = nuevaFecha
            }
            4 -> {
                val nuevoActivo = JOptionPane.showInputDialog("¿Está activo? (true/false):").toBoolean()
                autor.activo = nuevoActivo
            }
            else -> JOptionPane.showMessageDialog(null, "Opción no válida.")
        }
        autorService.actualizarAutor(autor.id, autor.nombre) // Actualiza solo el atributo nombre
        JOptionPane.showMessageDialog(null, "Autor actualizado con éxito.")
    } else {
        JOptionPane.showMessageDialog(null, "Autor no encontrado.")
    }
}


fun eliminarAutor(autorService: AutorService) {
    val autores = autorService.listarAutores()
    if (autores.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay autores registrados.")
        return
    }

    val listaAutores = autores.joinToString("\n") { "ID: ${it.id}, Nombre: ${it.nombre}" }
    val autorId = JOptionPane.showInputDialog("Seleccione el ID del Autor a eliminar:\n$listaAutores").toInt()

    val autor = autores.find { it.id == autorId }
    if (autor != null) {
        autorService.eliminarAutor(autorId)
        JOptionPane.showMessageDialog(null, "Autor eliminado con éxito.")
    } else {
        JOptionPane.showMessageDialog(null, "Autor no encontrado.")
    }
}


fun crearLibroConAutor(libroService: LibroService, autorService: AutorService) {
    val titulo = JOptionPane.showInputDialog("Ingrese el Título del Libro:")
    val genero = JOptionPane.showInputDialog("Ingrese el Género del Libro:")
    val precio = JOptionPane.showInputDialog("Ingrese el Precio del Libro:").toDouble()

    val autores = autorService.listarAutores()
    val listaAutores = autores.joinToString("\n") { "ID: ${it.id}, Nombre: ${it.nombre}" }
    val autorId = JOptionPane.showInputDialog("Seleccione el ID del Autor:\n$listaAutores").toInt()
    val autor = autores.find { it.id == autorId } ?: throw Exception("Autor no encontrado")

    val libro = Libro(0, titulo, genero, precio, autor)
    libroService.crearLibro(libro)
    JOptionPane.showMessageDialog(null, "Libro creado con éxito.")
}

fun listarLibrosConAutores(libroService: LibroService, autorService: AutorService) {
    val libros = libroService.listarLibros(autorService)
    val lista = libros.joinToString("\n") {
        "ID: ${it.id}, Título: ${it.titulo}, Género: ${it.genero},Precio ${it.precio}, Autor: ${it.autor.nombre}"
    }
    JOptionPane.showMessageDialog(null, if (libros.isNotEmpty()) lista else "No hay libros registrados.")
}


fun actualizarLibro(libroService: LibroService, autorService: AutorService) {
    val libros = libroService.listarLibros(autorService)
    if (libros.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay libros registrados.")
        return
    }

    val listaLibros = libros.joinToString("\n") { "ID: ${it.id}, Título: ${it.titulo}" }
    val libroId = JOptionPane.showInputDialog("Seleccione el ID del Libro a actualizar:\n$listaLibros").toInt()
    val libro = libros.find { it.id == libroId }

    if (libro != null) {
        val atributo = JOptionPane.showInputDialog(
            "Seleccione el atributo a actualizar:\n" +
                    "1. Título\n" +
                    "2. Género\n" +
                    "3. Precio\n" +
                    "4. Autor"
        ).toInt()

        when (atributo) {
            1 -> {
                val nuevoTitulo = JOptionPane.showInputDialog("Ingrese el nuevo Título:")
                libro.titulo = nuevoTitulo
            }
            2 -> {
                val nuevoGenero = JOptionPane.showInputDialog("Ingrese el nuevo Género:")
                libro.genero = nuevoGenero
            }
            3 -> {
                val nuevoPrecio = JOptionPane.showInputDialog("Ingrese el nuevo Precio:").toDouble()
                libro.precio = nuevoPrecio
            }
            4 -> {
                val autores = autorService.listarAutores()
                val listaAutores = autores.joinToString("\n") { "ID: ${it.id}, Nombre: ${it.nombre}" }
                val autorId = JOptionPane.showInputDialog("Seleccione el ID del nuevo Autor:\n$listaAutores").toInt()
                val nuevoAutor = autores.find { it.id == autorId }
                if (nuevoAutor != null) {
                    libro.autor = nuevoAutor
                } else {
                    JOptionPane.showMessageDialog(null, "Autor no encontrado.")
                    return
                }
            }
            else -> JOptionPane.showMessageDialog(null, "Opción no válida.")
        }

        libroService.actualizarLibro(libro.id, libro)
        JOptionPane.showMessageDialog(null, "Libro actualizado con éxito.")
    } else {
        JOptionPane.showMessageDialog(null, "Libro no encontrado.")
    }
}

fun eliminarLibro(libroService: LibroService, autorService: AutorService) {
    val libros = libroService.listarLibros(autorService)
    if (libros.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay libros registrados.")
        return
    }

    val listaLibros = libros.joinToString("\n") { "ID: ${it.id}, Título: ${it.titulo}, Autor: ${it.autor.nombre}" }
    val libroId = JOptionPane.showInputDialog("Seleccione el ID del Libro a eliminar:\n$listaLibros").toInt()

    val libro = libros.find { it.id == libroId }
    if (libro != null) {
        libroService.eliminarLibro(libroId)
        JOptionPane.showMessageDialog(null, "Libro eliminado con éxito.")
    } else {
        JOptionPane.showMessageDialog(null, "Libro no encontrado.")
    }
}


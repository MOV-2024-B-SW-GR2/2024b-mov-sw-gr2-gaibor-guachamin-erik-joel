package com.example.deber2_ejgg.controlador

import android.content.ContentValues
import android.content.Context
import com.example.deber2_ejgg.entidad.Libro
import com.example.deber2_ejgg.persistencia.SqliteHelper

class ControladorLibro(context: Context) {
    private val dbHelper = SqliteHelper(context)

    fun crearLibro(idAutor: Int, libro: Libro) {
        dbHelper.writableDatabase.use { db ->
            db.insertOrThrow("Libro", null, libro.toContentValues(idAutor))
        }
    }

    fun listarLibrosPorAutor(idAutor: Int): List<Libro> {
        val libros = mutableListOf<Libro>()
        dbHelper.readableDatabase.use { db ->
            db.rawQuery(
                "SELECT * FROM Libro WHERE autor_id = ?",
                arrayOf(idAutor.toString())
            ).use { cursor ->
                while (cursor.moveToNext()) {
                    libros.add(cursor.toLibro())
                }
            }
        }
        return libros
    }

    fun actualizarLibro(libro: Libro): Boolean {
        return dbHelper.writableDatabase.use { db ->
            val rowsUpdated = db.update(
                "Libro",
                libro.toContentValues(),
                "id = ?",
                arrayOf(libro.id.toString())
            )
            rowsUpdated > 0
        }
    }

    fun eliminarLibro(id: Int): Boolean {
        return dbHelper.writableDatabase.use { db ->
            val rowsDeleted = db.delete(
                "Libro",
                "id = ?",
                arrayOf(id.toString())
            )
            rowsDeleted > 0
        }
    }

    fun depurarLibros() {
        dbHelper.readableDatabase.use { db ->
            db.rawQuery("SELECT * FROM Libro", null).use { cursor ->
                while (cursor.moveToNext()) {
                    println(cursor.toLibro().formatToString())
                }
            }
        }
    }

    // Funciones auxiliares

    private fun Libro.toContentValues(idAutor: Int? = null): ContentValues {
        return ContentValues().apply {
            put("titulo", titulo)
            put("genero", genero)
            put("precio", precio)
            idAutor?.let {
                put("autor_id", it)
            }
        }
    }

    private fun android.database.Cursor.toLibro(): Libro {
        return Libro(
            id = getInt(getColumnIndexOrThrow("id")),
            titulo = getString(getColumnIndexOrThrow("titulo")),
            genero = getString(getColumnIndexOrThrow("genero")),
            precio = getDouble(getColumnIndexOrThrow("precio")),
            autor_id = getInt(getColumnIndexOrThrow("autor_id"))
        )
    }

    private fun Libro.formatToString(): String {
        return "Libro: $id, Título: $titulo, Género: $genero, Precio: $precio"
    }
}

package com.example.deber2_ejgg.controlador

import android.content.ContentValues
import android.content.Context
import com.example.deber2_ejgg.entidad.Autor
import com.example.deber2_ejgg.persistencia.SqliteHelper

class ControladorAutor(context: Context) {
    private val dbHelper = SqliteHelper(context)

    fun crearAutor(autor: Autor) {
        dbHelper.writableDatabase.use { db ->
            db.insert("Autor", null, autor.toContentValues())
        }
    }

    fun listarAutores(): List<Autor> {
        val autores = mutableListOf<Autor>()
        dbHelper.readableDatabase.use { db ->
            db.rawQuery("SELECT * FROM Autor", null).use { cursor ->
                while (cursor.moveToNext()) {
                    autores.add(cursor.toAutor())
                }
            }
        }
        return autores
    }

    fun actualizarAutor(autor: Autor): Boolean {
        return dbHelper.writableDatabase.use { db ->
            val rowsUpdated = db.update(
                "Autor",
                autor.toContentValues(),
                "id = ?",
                arrayOf(autor.id.toString())
            )
            rowsUpdated > 0
        }
    }

    fun eliminarAutor(id: Int): Boolean {
        return dbHelper.writableDatabase.use { db ->
            val rowsDeleted = db.delete(
                "Autor",
                "id = ?",
                arrayOf(id.toString())
            )
            rowsDeleted > 0
        }
    }

    private fun Autor.toContentValues(): ContentValues {
        return ContentValues().apply {
            put("nombre", nombre)
            put("nacionalidad", nacionalidad)
            put("fechaNacimiento", fechaNacimiento)
            put("activo", if (activo) 1 else 0)
            put("latitud" ,latitud )
            put("longitud" ,longitud )
        }
    }

    private fun android.database.Cursor.toAutor(): Autor {
        return Autor(
            getInt(getColumnIndexOrThrow("id")),
            getString(getColumnIndexOrThrow("nombre")),
            getString(getColumnIndexOrThrow("nacionalidad")),
            getString(getColumnIndexOrThrow("fechaNacimiento")),
            getInt(getColumnIndexOrThrow("activo")) == 1,
            mutableListOf(),
            getDouble(getColumnIndexOrThrow("latitud")),
            getDouble(getColumnIndexOrThrow("longitud"))
        )
    }
}

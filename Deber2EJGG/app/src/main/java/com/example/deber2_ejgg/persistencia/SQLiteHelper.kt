package com.example.deber2_ejgg.persistencia


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context?) : SQLiteOpenHelper(
    context,
    "LibreriaDB",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSqlCrearAutor = """
            CREATE TABLE Autor (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(250),
                nacionalidad VARCHAR(250),
                fechaNacimiento VARCHAR(250),
                activo INTEGER
            )
        """.trimIndent()
        db?.execSQL(scriptSqlCrearAutor)

        val scriptSqlCrearLibro = """
            CREATE TABLE Libro (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo VARCHAR(250),
                genero VARCHAR(250),
                precio REAL,
                autor_id INTEGER,
                FOREIGN KEY (autor_id) REFERENCES Autor(id) ON DELETE CASCADE
            )
        """.trimIndent()
        db?.execSQL(scriptSqlCrearLibro)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS Libro")
            db?.execSQL("DROP TABLE IF EXISTS Autor")
            onCreate(db) // Recrea las tablas
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true) // Habilita las claves foráneas
    }
}

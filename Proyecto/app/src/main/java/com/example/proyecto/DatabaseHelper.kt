package com.example.proyecto

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mi_base_de_datos.db"
        private const val DATABASE_VERSION = 7// Asegúrate de incrementar la versión de la base de datos
        private const val TABLE_BICICLETAS = "bicicletas"
        private const val COLUMN_ID = "id"
        private const val COLUMN_MARCA = "marca"
        private const val COLUMN_MODELO = "modelo"
        private const val COLUMN_ANIO = "anio"
        private const val COLUMN_IMAGE_PATH = "imagePath"
        private const val COLUMN_NUM_SERIE = "numero_serie"
        private const val COLUMN_FECHA_COMPRA = "fechaCompra"  // Nueva columna fechaCompra
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_BICICLETA_TABLE = "CREATE TABLE $TABLE_BICICLETAS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_MARCA TEXT," +
                "$COLUMN_MODELO TEXT," +
                "$COLUMN_ANIO INTEGER," +
                "$COLUMN_IMAGE_PATH TEXT," +
                "$COLUMN_NUM_SERIE TEXT," +
                "$COLUMN_FECHA_COMPRA TEXT)"

        val CREATE_REPAIRS_TABLE = "CREATE TABLE repairs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bicicleta_id INTEGER," +
                "fecha TEXT," +
                "tipo TEXT," +
                "repuestos TEXT," +
                "precio REAL," +
                "observacion TEXT," +
                "FOREIGN KEY(bicicleta_id) REFERENCES $TABLE_BICICLETAS($COLUMN_ID))"



        db?.execSQL(CREATE_BICICLETA_TABLE)
        db?.execSQL(CREATE_REPAIRS_TABLE)

    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < DATABASE_VERSION) {
            db?.execSQL("DROP TABLE IF EXISTS repairs")
            onCreate(db)
        }
    }
    // Método para agregar un vehículo
    fun agregarBicicleta(bicicleta: Bicicleta): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MARCA, bicicleta.marca)
            put(COLUMN_MODELO, bicicleta.modelo)
            put(COLUMN_ANIO, bicicleta.anio)
            put(COLUMN_IMAGE_PATH, bicicleta.imagePath)
            put(COLUMN_NUM_SERIE, bicicleta.num_serie)
            put(COLUMN_FECHA_COMPRA, bicicleta.fechaCompra)  // Agregar fechaCompra
        }
        return db.insert(TABLE_BICICLETAS, null, values)
    }

    // Método para obtener todos los vehículos
    fun obtenerBicicletas(): List<Bicicleta> {
        val bicicletaList = mutableListOf<Bicicleta>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_BICICLETAS", null)

        val columnIndexId = cursor.getColumnIndex(COLUMN_ID)
        val columnIndexMarca = cursor.getColumnIndex(COLUMN_MARCA)
        val columnIndexModelo = cursor.getColumnIndex(COLUMN_MODELO)
        val columnIndexAnio = cursor.getColumnIndex(COLUMN_ANIO)
        val columnIndexImagePath = cursor.getColumnIndex(COLUMN_IMAGE_PATH)
        val columnIndexNumSerie = cursor.getColumnIndex(COLUMN_NUM_SERIE)
        val columnIndexFechaCompra = cursor.getColumnIndex(COLUMN_FECHA_COMPRA)

        if (cursor.moveToFirst()) {
            do {
                val bicicleta = Bicicleta(
                    id = cursor.getInt(columnIndexId),
                    marca = cursor.getString(columnIndexMarca) ?: "",  // Si es null, asigna un valor vacío
                    modelo = cursor.getString(columnIndexModelo) ?: "",  // Si es null, asigna un valor vacío
                    anio = cursor.getInt(columnIndexAnio),
                    imagePath = cursor.getString(columnIndexImagePath) ?: "",  // Si es null, asigna un valor vacío
                    num_serie = cursor.getString(columnIndexNumSerie) ?: "",  // Si es null, asigna un valor vacío
                    fechaCompra = cursor.getString(columnIndexFechaCompra) ?: ""  // Si es null, asigna un valor vacío
                )
                bicicletaList.add(bicicleta)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return bicicletaList
    }

    // Método para obtener un vehículo por ID
    fun obtenerBicicletaPorId(id: Int): Bicicleta? {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_BICICLETAS WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val marcaIndex = cursor.getColumnIndex(COLUMN_MARCA)
            val modeloIndex = cursor.getColumnIndex(COLUMN_MODELO)
            val anioIndex = cursor.getColumnIndex(COLUMN_ANIO)
            val imagePathIndex = cursor.getColumnIndex(COLUMN_IMAGE_PATH)
            val numSerieIndex = cursor.getColumnIndex(COLUMN_NUM_SERIE)
            val fechaCompraIndex = cursor.getColumnIndex(COLUMN_FECHA_COMPRA)

            if (idIndex == -1 || marcaIndex == -1 || modeloIndex == -1 || anioIndex == -1 || imagePathIndex == -1 || numSerieIndex == -1 || fechaCompraIndex == -1) {
                cursor.close()
                return null
            }

            val bicicleta = Bicicleta(
                id = cursor.getInt(idIndex),
                marca = cursor.getString(marcaIndex),
                modelo = cursor.getString(modeloIndex),
                anio = cursor.getInt(anioIndex),
                imagePath = cursor.getString(imagePathIndex),
                num_serie = cursor.getString(numSerieIndex),
                fechaCompra = cursor.getString(fechaCompraIndex)
            )

            cursor.close()
            return bicicleta
        }

        cursor.close()
        return null
    }
    // Método para eliminar un vehículo por ID
    fun eliminarBicicleta(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_BICICLETAS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Método para actualizar un vehículo existente
    fun actualizarBicicleta(bicicleta: Bicicleta): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MARCA, bicicleta.marca)
            put(COLUMN_MODELO, bicicleta.modelo)
            put(COLUMN_ANIO, bicicleta.anio)
            put(COLUMN_IMAGE_PATH, bicicleta.imagePath)
            put(COLUMN_NUM_SERIE, bicicleta.num_serie)
            put(COLUMN_FECHA_COMPRA, bicicleta.fechaCompra)
        }

        // Actualiza el vehículo en la base de datos donde el ID coincide
        return db.update(TABLE_BICICLETAS, values, "$COLUMN_ID = ?", arrayOf(bicicleta.id.toString()))
    }


    fun agregarReparacion(reparacion: Reparacion): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("bicicleta_id", reparacion.bicicletaId)
            put("fecha", reparacion.fecha)
            put("tipo", reparacion.tipo)
            put("repuestos", reparacion.repuestos)
            put("precio", reparacion.precio)
            put("observacion", reparacion.observacion)
        }
        return db.insert("repairs", null, values)
    }

    fun obtenerReparacionesPorBicicleta(bicicleta_id: Int): List<Reparacion> {
        val reparacionList = mutableListOf<Reparacion>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM repairs WHERE bicicleta_id = ?", arrayOf(bicicleta_id.toString()))

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex("id")
                val bicicletaIdIndex = cursor.getColumnIndex("bicicleta_id")
                val fechaIndex = cursor.getColumnIndex("fecha")
                val tipoIndex = cursor.getColumnIndex("tipo")
                val repuestosIndex = cursor.getColumnIndex("repuestos")
                val precioIndex = cursor.getColumnIndex("precio")
                val observacionIndex = cursor.getColumnIndex("observacion")

                // Validación para evitar -1
                if (idIndex != -1 && bicicletaIdIndex != -1 && fechaIndex != -1 &&
                    tipoIndex != -1 && repuestosIndex != -1 && precioIndex != -1 && observacionIndex != -1) {

                    val reparacion = Reparacion(
                        id = cursor.getInt(idIndex),
                        bicicletaId = cursor.getInt(bicicletaIdIndex),
                        fecha = cursor.getString(fechaIndex),
                        tipo = cursor.getString(tipoIndex),
                        repuestos = cursor.getString(repuestosIndex),
                        precio = cursor.getDouble(precioIndex),
                        observacion = cursor.getString(observacionIndex)
                    )
                    reparacionList.add(reparacion)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return reparacionList
    }







}


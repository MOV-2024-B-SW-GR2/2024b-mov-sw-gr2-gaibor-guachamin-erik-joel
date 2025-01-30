import com.example.deber1.modelos.Autor
import com.example.deber1.modelos.Libro

class BDmemoria {
    companion object {
        val autores = mutableListOf(
            Autor(
                id = 1,
                nombre = "Autor 1",
                nacionalidad = "Ecuador",
                fechaNacimiento = "1970-01-01",
                activo = true,
                libros = mutableListOf(
                    Libro(1, "Libro 1", "Ficción", 19.99, 1),
                    Libro(2, "Libro 2", "Historia", 24.99, 1)
                )
            ),
            Autor(
                id = 2,
                nombre = "Autor 2",
                nacionalidad = "Perú",
                fechaNacimiento = "1980-05-05",
                activo = false,
                libros = mutableListOf(
                    Libro(3, "Libro 3", "Ciencia", 29.99, 2),
                    Libro(4, "Libro 4", "Fantasía", 34.99, 2)
                )
            )
        )
    }
}

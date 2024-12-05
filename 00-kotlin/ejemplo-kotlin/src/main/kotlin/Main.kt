package org.example


fun main(arg:Array<String>) {
    /*
        // INMUTABLES (No se RE ASIGNA "=")

        val inmutable: String = "Erik";
        // inmutable = "Joel" // Error!

        // MUTABLES

        var mutable: String = "Erik"

        mutable = "Joel" // 0k

        //  VAL > VAR


        // Duck Typing
        val ejemploVariable = " Erik Gaibor"
        ejemploVariable.trim()
        val edadEjemplo: Int = 12
        // ejemploVariable = edadEjemplo // Error!
        // Variables Primitivos
        val nombreProfesor: String = "Adrian Eguez"
        val sueldo: Double = 1.2
        val estadoCivil: Char = 'C'
        val mayorEdad: Boolean = true
        // Clases en Java
        val fechaNacimiento: Date = Date()


        val estadoCivilWhen = "C"
        when (estadoCivilWhen) {
            ("C") -> {
                println("Casado")
            }

            "S" -> {
                println("Soltero")
            }

            else -> {
                println("No sabemos")

            }
        }
        //val esSoltero = (estadoCivilWhen = "S")
        //val coqueteo = if (esSoltero) "Si" else "No" // if else chiquito

        imprimirNombre("JOEL")

        calcularSueldo(10.00) // solo paramtro reguerido
        calcularSueldo(10.00,15.00,20.00) // parametro requerido y sobreescribir parametros opcionales
        // Named parameters
        // calcularSueldo(sueldo, tasa, bonoEspecial)
        calcularSueldo(10.00, bonoEspecial = 20.00) // usando el parametro bonoEspecial en 2da posicion
        // gracias a los parametros nombrados

        calcularSueldo(bonoEspecial = 20.00, sueldo=10.00, tasa=14.00)
        // usando el parametro bonoEspecial en 1ra posicion

        // usando el parametro sueldo en 2da posicion

        // usando el parametro tasa en 3era posicion
        // gracias a los parametros nombrados*/

    /*  val sumaA = Suma(1,1)
    val sumaB = Suma(null, 1)
    val sumaC = Suma(1, null)
    val sumaD = Suma(null, null)
    sumaA.sumar()
    sumaB.sumar()
    sumaC.sumar()
    sumaD.sumar()
    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)
*/

    // Arreglos
    // Estoticos
    val arregloEstatico: Array<Int> = arrayOf<Int>(1, 2, 3)
    println(arregloEstatico);
    // Dinamicos
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    )

    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)


    // FOR EACH = > Unit
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach { valorActual: Int -> //  - > = >
            println("Valor actual: ${valorActual}");
        }
    // "it" (en ingles "eso") significa el elemento iterado
    arregloDinamico.forEach{ println("Valor Actual (it): ${it}")}

    // MAP -> MUTA(Modifica cambia) el arreglo
    // 1) Enviamos el nuevo valor de la iteracion
    // 2) Nos devuelve un NUEVO ARREGLO con valores
    // de las iteraciones
    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }
    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map{ it + 15 }
    println(respuestaMapDos)

    // Filter - > Filtrar el ARREGLO
    // 1) Devolver una expresion (TRUE o FALSE)
    // 2) Nuevo arreglo FILTRADO
    val respuestaFilter: List<Int> = arregloDinamico
        .filter{ valorActual:Int ->
            // Expresion o CONDICION
            val mayoresACinco: Boolean = valorActual > 5
            return@filter mayoresACinco
        }

    val respuestaFilterDos =arregloDinamico.filter{ it <=5 }
    println(respuestaFilter)
    println(respuestaFilterDos)

    // OR AND
    // OR -> ANY (Alguno Cumple?)
    // And -> ALL (Todos cumplen?)
    val respuestaAny: Boolean = arregloDinamico
        .any{ valorActual:Int ->
            return@any (valorActual > 5)
        }
    println(respuestaAny) // True
    val respuestaAll: Boolean = arregloDinamico
        .all{ valorActual:Int ->
            return@all (valorActual > 5)
        }
    println(respuestaAll) // False


    // REDUCE -> Valor acumulado
    // Valor acumulado = 0 (Siempre empieza en 0 en Kotlin)
    // [1,2,3,4,5] -> Acumular "SUMAR" estos valores del arreglo
    // valorIteracion1 = valorEmpieza + 1 = 0 +1 = 1 -> Iteracion1
    // valorIteracion2 = valorAcumuladoIteracion1 + 2 = 1 + 2 = 3 -> Iteracion2
    // valorIteracion3 = valorAcumuladoIteracion2 + 3 = 3 + 3 = 6 -> Iteracion3
    // valorIteracion4 = valorAcumuladoIteracion3 + 4 = 6 + 4 = 10 -> Iteracion4
    // valorIteracion5 = valorAcumuladoIteracion4 + 5 = 10 + 5 = 15 -> Iteracion4
    val respuestaReduce: Int = arregloDinamico
        .reduce{ acumulado:Int, valorAcual:Int ->
            return@reduce (acumulado + valorAcual) // -> Cambiar o usar la logica de negocio
        }
    println(respuestaReduce);
    // return@reduce acumulado + (itemCarrito.cantidad * itemCarrito.precio)




}



fun calcularSueldo(

    sueldo:Double, // Requerido
    tasa: Double = 12.00, // Opcional (defecto)
    bonoEspecial:Double? = null // Opcional (nullable)
// Variable? -> "?" Es Nullable (osea que puede en algun momento ser nulo)

) :Double {
// Int -> Int? (nullable)
// String -> String? (nullable)
// Date -> Date? (nullable)
    if (bonoEspecial == null) {
        return sueldo * (100 / tasa)
    } else {

        return sueldo * (100 / tasa) * bonoEspecial

    }
}


abstract class NumerosJava {
    protected val numeroUno: Int
    private val numeroDos: Int

    constructor(

        uno: Int,

        dos: Int

    ) {
        this.numeroUno = uno

        this.numeroDos = dos

        println("Inicializando")
    }
}

abstract class Numeros( // Constructor Primario
    // Caso 1) Parametro normol

    // uno:Int , (parametro (sin modificador acceso))

    // Caso 2) Parametro y propiedad (atributo) (protected)
    //private var uno: Int (propiedad "instancio.uno")

    protected val numeroUno: Int, // instoncio.numeroUno
    protected val numeroDos: Int, // instancio. numeroDos

    parametroNoUsadoNoPropiedadDeLaClase: Int? = null ){

    init { // bloque constructor primario OPCIONAL

        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}

class Suma( // Constructor primario
    unoParametro: Int, // Parametro
    dosParametro: Int, // Parametros
): Numeros( // Clase papa, Numeros (extendiendo)
    unoParametro,
    dosParametro
){
    public val soyPublicoExplicito: String = "Publicas"
    val soyPublicoImplicito: String = "Publico implicito"

    init {
        this.numeroUno
        this.numeroDos
        numeroUno //this. OPCIONAL {propiedades , métodos }
        numeroDos
        this.soyPublicoImplicito
        soyPublicoExplicito
    }

    constructor( // Constructor secundarios
        uno: Int?, //Entero nullable
        dos: Int
    ):this(

        if(uno == null) 0 else uno,dos
    ){
        //Bloque de codigo secundario
    }

    constructor( // Constructor secundarios
        uno: Int,
        dos: Int?//Entero nullable
    ):this(
        uno,
        if(dos == null) 0 else dos
    ){
        //Bloque de codigo secundario
    }
    constructor( // Constructor secundarios
        uno: Int?, //Entero nullable
        dos: Int?//Entero nullable
    ):this(

        if(uno == null) 0 else uno,
        if(uno == null) 0 else dos
    ){
        //Bloque de codigo secundario
    }


    fun sumar ():Int {
        val total = numeroUno +numeroDos
        agregarHistorial(total)
        return total
    }
    companion object { // Comparte entre todas las instancias, similar al STATIC
        // funciones, variables
        // NombreClase, metodo,
        // Suma.pi
        val pi = 3.14

        // Suma.elevarAlCuadrada
        fun elevarAlCuadrado(num: Int): Int {
            return num * num
        }

        val historialSumas = arrayListOf<Int>()

        fun agregarHistorial(valorTotalSuma: Int) { //Suma.agregarHistorial
            historialSumas.add(valorTotalSuma)
        }

    }

}

/*
fun imprimirNombre(nombre: String): Unit {
    fun otraFuncionAdentro() {
        println("Otra funcion adentro")

        println("Nombre: $nombre") // Uso sin Llaves
        println("Nombre: ${nombre}") // Uso con Llaves opcional
        println("Nombre: ${nombre + nombre}") // Uso con Llaves (concatenado)
        println("Nombre: ${nombre.toString()}") // Uso con Llaves (funcion)
        println("Nombre: $nombre.toString()") // INCORRECTO!
        // (no pueden usar sin llaves)
    }

    // Call the inner function here
    otraFuncionAdentro()
}
     */
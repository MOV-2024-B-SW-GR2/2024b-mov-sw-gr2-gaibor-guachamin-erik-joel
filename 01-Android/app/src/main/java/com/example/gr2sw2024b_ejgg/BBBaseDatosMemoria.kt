package com.example.gr2sw2024b_ejgg

class BBBaseDatosMemoria {
    companion object{
        var arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1,"Erik","e@e.com"))
            arregloBEntrenador.add(BEntrenador(2,"Erik1","e@1.com"))
            arregloBEntrenador.add(BEntrenador(3,"Erik2","e@2.com"))
        }
    }
}
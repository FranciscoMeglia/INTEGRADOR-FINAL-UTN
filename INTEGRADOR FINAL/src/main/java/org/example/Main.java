package org.example;


public class Main {
    public static void main(String[] args) {

           // Creo una instancia de Apuestas
           Apuestas apuestas = new Apuestas();

        /* Imprimo el puntaje de cada participante a traves del meotodo calcularPuntaje() y le paso por
           parametro la cantidad de puntos que quiero que de por aciertos y la cantidad de puntos que quiero
           que de si el participante le acierta al resultado de todos los partidos */
           System.out.println("-----------------------------");
           apuestas.calcularPuntajes(Integer.parseInt(args[0]) , Integer.parseInt(args[1]));
           System.out.println("-----------------------------");

    }
}
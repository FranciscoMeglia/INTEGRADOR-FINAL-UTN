package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Apuestas {


    // BASE DE DATOS MYSQL
    String url = "jdbc:mysql://localhost:3306/integradorutn";
    String usuario = "root";
    String password = "scorpion2004gg";

    // MENSAJE QUE SE IMPRIME SI OCURRE ALGUN ERROR A LA HORA DE CONECTARSE A LA BASE DE DATOS
    String error = "No se ha podido conectar a la base de datos correctamente";

    // OBTENER PRONOSTICOS
    public HashMap<Participante, ArrayList<String>> obtenerPronosticos() {

       // ArrayList<String> lineasPronosticos = new ArrayList<String>();
        HashMap<Participante, ArrayList<String>> pronosticos = new HashMap<>();
        HashMap<String, Participante> participantes = new HashMap<>();

        try {
            Connection conexion = DriverManager.getConnection(url, usuario, password);
            Statement statement = conexion.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM integradorutn.pronosticos");
            while (resultSet.next()) {
                String nombre = resultSet.getString("Nombre");
                String equipo1 = resultSet.getString("Equipo1");
                String ganaEquipo1 = resultSet.getString("GanaEquipo1");
                String empate = resultSet.getString("Empate");
                String ganaEquipo2 = resultSet.getString("GanaEquipo2");
                String equipo2 = resultSet.getString("Equipo2");
                String pronostico = (nombre + ":" + equipo1 + ":" + ganaEquipo1 + ":" + empate + ":" + ganaEquipo2 + ":" + equipo2);

                if (!participantes.containsKey(nombre)) {
                    Participante participante = new Participante(nombre);
                    participantes.put(nombre, participante);
                    ArrayList<String> pronosticosParticipante = new ArrayList<>();
                    pronosticosParticipante.add(pronostico);
                    pronosticos.put(participante, pronosticosParticipante);
                } else { // Si ya existe el participante, agregamos el pronóstico a su lista
                    Participante participante = participantes.get(nombre);
                    pronosticos.get(participante).add(pronostico);
                }

            }
            conexion.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(error);
        }

        return pronosticos;
    }



    // OBTENER RESULTADOS
    public ArrayList<String> obtenerResultados() {

        ArrayList<String> resultados = new ArrayList<>();

        try {
            Connection conexion = DriverManager.getConnection(url, usuario, password);
            Statement statement = conexion.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM integradorutn.resultados");
            while (resultSet.next()) {

                String equipo1 = resultSet.getString("Equipo1");
                int golesEquipo1 = resultSet.getInt("GolesEquipo1");
                int golesEquipo2 = resultSet.getInt("GolesEquipo2");
                String equipo2 = resultSet.getString("Equipo2");
                String resultado = (equipo1 + ":" + golesEquipo1 + ":" + golesEquipo2 + ":" + equipo2);
                resultados.add(resultado);

            }
            conexion.close();
            statement.close();
            resultSet.close();
        } catch(SQLException e) {
                System.out.println(error);
            }
        return resultados;
    }


    // CALCULAR PUNTAJE COMPARANDO LOS PRONOSTICOS REALIZADOS DE CADA PARTICIPANTE CON LOS RESULTADOS
    public void calcularPuntajes(int puntosExtra , int puntosPorAcierto) {
        HashMap<Participante, ArrayList<String>> pronosticos = obtenerPronosticos();
        ArrayList<String> resultados = obtenerResultados();

        for (Participante participante : pronosticos.keySet()) {
            int puntaje = 0;
            int aciertos = 0;
            for (String pronostico : pronosticos.get(participante)) {
                String[] datosPronostico = pronostico.split(":");
                String equipo1 = datosPronostico[1];
                String ganaEquipo1 = datosPronostico[2];
                String empate = datosPronostico[3];
                String ganaEquipo2 = datosPronostico[4];
                String equipo2 = datosPronostico[5];

                for (String resultado : resultados) {
                    String[] datosResultado = resultado.split(":");
                    String equipo1Resultado = datosResultado[0];
                    int golesEquipo1 = Integer.parseInt(datosResultado[1]);
                    int golesEquipo2 = Integer.parseInt(datosResultado[2]);
                    String equipo2Resultado = datosResultado[3];

                    boolean acertado = false;

                    if (ganaEquipo1.equals("X")) {
                        if (golesEquipo1 > golesEquipo2 && equipo1.equals(equipo1Resultado) && equipo2.equals(equipo2Resultado)) {
                            acertado = true;
                        }
                    } else if (empate.equals("X")) {
                        if (golesEquipo1 == golesEquipo2 && equipo1.equals(equipo1Resultado) && equipo2.equals(equipo2Resultado)) {
                            acertado = true;
                        }
                    } else if (ganaEquipo2.equals("X")) {
                        if (golesEquipo1 < golesEquipo2 && equipo1.equals(equipo1Resultado) && equipo2.equals(equipo2Resultado)) {
                            acertado = true;
                        }
                    }

                    if (acertado) {
                        puntaje += puntosPorAcierto;
                        if (resultados.indexOf(resultado) == pronosticos.get(participante).size() - 1) {
                            aciertos++;
                        }
                    }
                }
            }

            if (aciertos == resultados.size()) {
                puntaje += puntosExtra;
            }

            System.out.println(participante.getNombre() + ": " + puntaje + " puntos");

        }
    }
}




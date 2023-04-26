package org.example;

import lombok.Getter;
import lombok.Setter;

public class Participante {

    @Setter @Getter
    private String nombre;

    public Participante(String nombre) {
        this.nombre = nombre;
    }

}

package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Idioma {

    ESPANIOL("es", "Español"),
    INGLES("en", "Inglés"),
    FRANCES("fr", "Francés"),
    PORTUGUES("pt", "Portugués");

    private String idioma;
    private String descripcion;

    Idioma(String idioma, String descripcion) {
        this.idioma = idioma;
        this.descripcion = descripcion;
    }

    @JsonCreator
    public static Idioma fromString(String texto) {
        for (Idioma idioma : Idioma.values())
            if (idioma.idioma.equals(texto))
                return idioma;
        System.out.println("Ninguna idioma encontrado: " + texto);
        return ESPANIOL;
    }

    @JsonValue
    public String getIdioma() {
        return idioma;
    }
}

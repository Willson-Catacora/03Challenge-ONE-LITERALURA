package com.alura.literalura.model;

import java.util.*;
import java.util.Optional;

public class Libro {
    String titulo;
    List<Autor> autores;
    List<String> idiomas;
    Integer descargas;

    public Libro(Optional<DatosLibro> datosLibro) {
        this.autores = new ArrayList<>();
        this.titulo = datosLibro.get().titulo();
        datosLibro.get().autores().forEach(
                a -> this.autores.add(new Autor(a.nombre(), a.fechaDeNacimiento(), a.fechaDeFallecimiento()))
        );
        this.idiomas = datosLibro.get().idiomas();
        this.descargas = datosLibro.get().descargas();
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", autores=" + autores +
                ", idiomas=" + idiomas +
                ", descargas=" + descargas +
                '}';
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }
}

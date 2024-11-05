package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.*;
import java.util.Optional;

@Entity
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @ElementCollection(targetClass = Idioma.class)
    @CollectionTable(name = "idiomas_libro", joinColumns = @JoinColumn(name = "id_libro"))
    @Enumerated(EnumType.STRING) // Usamos EnumType.STRING para almacenar los nombres de los enums como texto
    @Column(name = "idioma")
    Set<Idioma> idiomas;
    private Integer descargas;
    @ManyToMany
    @JoinTable(
            name = "libro_autor", // Tabla intermedia
            joinColumns = @JoinColumn(name = "libro_id"), // Columna de la entidad Libro
            inverseJoinColumns = @JoinColumn(name = "autor_id") // Columna de la entidad Autor
    )
    private Set<Autor> autores = new HashSet<>();

    public Libro() {
    }

    public Libro(String titulo, Set<Autor> autor, Set<Idioma> idiomas, Integer descargas) {
        this.titulo = titulo;
        this.autores = autor;
        this.idiomas = idiomas;
        this.descargas = descargas;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autor) {
        this.autores = autor;
    }

    public Set<Idioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Set<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }
}

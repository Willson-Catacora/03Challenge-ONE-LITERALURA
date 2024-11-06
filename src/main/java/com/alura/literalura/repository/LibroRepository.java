package com.alura.literalura.repository;

import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l " +
            "JOIN FETCH l.autores a " +
            "JOIN FETCH l.idiomas il")
    List<Libro> busquedaLibros();

    @Query("SELECT l FROM Libro l " +
            "JOIN FETCH l.idiomas il " +
            "WHERE il = :idioma")
    List<Libro> busquedaLibrosPorIdioma(Idioma idioma);

    @Query("SELECT l FROM Libro l " +
            "JOIN FETCH l.autores "+
            "JOIN FETCH l.idiomas il " +
            "WHERE l.titulo = :titulo")
    Libro findByTitulo(String titulo);
}

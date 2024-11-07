package com.alura.literalura.repository;

import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l " +
            "JOIN FETCH l.autores a " +
            "JOIN FETCH l.idiomas il")
    List<Libro> busquedaLibros();

    @Query("SELECT l FROM Libro l " +
            "JOIN FETCH l.autores a " +
            "JOIN FETCH l.idiomas il " +
            "WHERE il = :idioma")
    List<Libro> busquedaLibrosPorIdioma(Idioma idioma);

    @Query("SELECT l FROM Libro l " +
            "JOIN FETCH l.autores " +
            "JOIN FETCH l.idiomas il " +
            "WHERE l.titulo = :titulo")
    Libro busquedaLibroPorTitulo(String titulo);

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autores LEFT JOIN FETCH l.idiomas ORDER BY l.descargas DESC LIMIT 10")
    List<Libro> findTop10ByOrderByDescargasDesc();

    Optional<Libro> findByTitulo(String titulo);
}

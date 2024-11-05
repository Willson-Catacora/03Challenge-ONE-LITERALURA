package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Autor findByNombre(String nombre);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros ")
    List<Autor> listaAutor();

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros WHERE a.fechaDeNacimiento <= :anio AND (a.fechaDeFallecimiento >= :anio OR a.fechaDeFallecimiento IS NULL)")
    List<Autor> autorVivoPorAnio4(int anio);
}

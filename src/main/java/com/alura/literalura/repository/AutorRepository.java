package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AutorRepository extends JpaRepository<Autor, Long>{
    Autor findByNombre(String nombre);
}

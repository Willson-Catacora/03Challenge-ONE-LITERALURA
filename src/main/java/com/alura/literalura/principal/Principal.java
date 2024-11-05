package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.*;
import com.alura.literalura.service.*;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumirAPI consumirApi = new ConsumirAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    private final String URL_BASE = "https://gutendex.com/books/";
    private Integer opcion = -1;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu() {
        while (opcion != 0) {
            var menu = """
                    ***==================================================***
                    \t\tElija una de las opción a travéz  de su número
                    ***==================================================***
                    \t1 - buscar libro por título
                    \t2 - listar libros registrados
                    \t3 - listar autores registrados
                    \t4 - listar autores vivos en determinado año
                    \t5 - listar libros por idioma
                    
                    \t0  - Salir
                    ***==================================================***
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 0:
                    System.out.println("\nCerrando la aplicación .......");
                    break;
                default:
                    System.out.println("\nIntroduzca una opción válida!");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("\nIngrese el nombre del libro que desee buscar");
        var libroBuscado = teclado.nextLine();
        var json = consumirApi.obtenerDatos(URL_BASE + "?search=" + libroBuscado.toLowerCase().replace(" ", "%20"));
        var datos = convierteDatos.obtenerDatos(json, Datos.class);
        if (!datos.libro().isEmpty()) {
            DatosLibro datosLibro = datos.libro().get(0);
            System.out.println(datosLibro.autor());
            Set<Autor> autor = datosLibro.autor().stream()
                    .map(a -> {
                        Autor autorExistente = autorRepository.findByNombre(a.nombre());
                        if (autorExistente != null) {
                            return autorExistente; // Si ya existe, usar el autor encontrado en la base de datos
                        } else {
                            return autorRepository.save(new Autor(a.nombre(), a.fechaDeNacimiento(), a.fechaDeFallecimiento()));
                        }
                    })
                    .collect(Collectors.toSet());
            System.out.println(autor.toString());
            Libro libro = new Libro(datosLibro.titulo(), autor, new HashSet<>(datosLibro.idiomas()), datosLibro.descargas());
            System.out.println(libro.toString());
            libroRepository.save(libro);
        } else
            System.out.println("Libro no encontrado");
    }

    private void listarLibrosRegistrados() {
        List<Libro> listaLibros = libroRepository.busquedaLibros();
        listaLibros.forEach(l -> {
            System.out.println("=========== *** LIBRO *** ===========");
            System.out.println("Titulo: " + l.getTitulo());
            System.out.println("Autores: " + l.getAutores().size());
            l.getAutores().forEach(
                    a -> {
                        System.out.println("\t" + a.getNombre());
                    }
            );
            System.out.println("Idiomas: ");
            l.getIdiomas().forEach(
                    i -> {
                        System.out.println("\t" + i + " - " + i.getIdioma());
                    }
            );
            System.out.println("Descargas: " + l.getDescargas());
            System.out.println("====================================");
        });
        System.out.println("\n");
    }
}

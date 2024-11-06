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
                    ***========================================================***
                    \t\tElija una de las opción a travéz  de su número
                    ***========================================================***
                    \t1 - buscar libro por título
                    \t2 - listar libros registrados
                    \t3 - listar autores registrados
                    \t4 - listar autores vivos en determinado año
                    \t5 - listar libros por idioma
                    
                    \t0  - Salir
                    ***========================================================***
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
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosDeterminadoAnio();
                    break;
                case 5:
                    listaDeLibrosPorIdiomas();
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
            libroRepository.save(new Libro(datosLibro.titulo(), autor, new HashSet<>(datosLibro.idiomas()), datosLibro.descargas()));
            Libro l = libroRepository.findByTitulo(datosLibro.titulo());
            System.out.println("============= *** LIBRO *** =============");
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
            System.out.println("=========================================");
        } else
            System.out.println("Libro no encontrado");
    }

    private void listarLibrosRegistrados() {
        List<Libro> listaLibros = libroRepository.busquedaLibros();
        listaLibros.forEach(l -> {
            System.out.println("============= *** LIBRO *** =============");
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
            System.out.println("=========================================");
        });
        System.out.println("\n");
    }

    private void listarAutoresRegistrados() {
        List<Autor> listarAutores = autorRepository.listaAutor();
        listarAutores.forEach(
                a -> {
                    System.out.println("============= *** Autor *** =============");
                    System.out.println("Nombre: " + a.getNombre());
                    System.out.println("Año de nacimiento: " + a.getFechaDeNacimiento());
                    System.out.println("Año de fallecimiento: " + a.getFechaDeFallecimiento());
                    System.out.println("Libros: ");
                    a.getLibros().forEach(
                            l -> {
                                System.out.println("\t" + l.getTitulo());
                            }
                    );
                    System.out.println("=========================================");
                }
        );
        System.out.println("\n");
    }

    private void listarAutoresVivosDeterminadoAnio() {
        System.out.println("Introduzca el año en el que desea buscar para ver autor(es) que estaban vivos");
        int anio = teclado.nextInt();
        teclado.nextLine();
        List<Autor> listarAutores = autorRepository.autorVivoPorAnio(anio);
        listarAutores.forEach(
                a -> {
                    System.out.println("=========== *** Autor *** ===========");
                    System.out.println("Nombre: " + a.getNombre());
                    System.out.println("Año de nacimiento: " + a.getFechaDeNacimiento());
                    System.out.println("Año de fallecimiento: " + a.getFechaDeFallecimiento());
                    System.out.println("Libros: ");
                    a.getLibros().forEach(
                            l -> {
                                System.out.println("\t" + l.getTitulo());
                            }
                    );
                    System.out.println("=====================================");
                }
        );
        System.out.println("\n");
    }

    private void listaDeLibrosPorIdiomas() {
        int[] index = {1};
        System.out.println("Introduzca un idioma que desee buscar " +
                "(es - español , en - inglés,  fr - francés, pt - protugés)");
        String lenguaje = teclado.nextLine().toLowerCase();
        Idioma idioma = Idioma.fromString(lenguaje);
        List<Libro> listaLibros = libroRepository.busquedaLibrosPorIdioma(idioma);
        System.out.println("Idioma: " + idioma + " - Cantidad de libros: " + listaLibros.size());
        listaLibros.forEach(l -> {
            System.out.println("================(" + index[0] + ")==================");
            System.out.println("Titulo: " + l.getTitulo());
            System.out.println("Autores: ");
            l.getAutores().forEach(a -> {
                System.out.println(a.getNombre());
            });
            l.getIdiomas().forEach(i -> {
                System.out.println("Idioma: " + i.getIdioma() + " => " + i);
            });
            System.out.println("=====================================");
            index[0]++;
        });
        System.out.println("\n");
    }
}

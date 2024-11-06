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
                    (Extra)
                    \t6 - generar estadisticas
                    \t7 - top 10 libros mas descargados (libros registrados)
                    \t8 - buscar autor por nombre
                    
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
                case 6:
                    estadisticas();
                    break;
                case 7:
                    top10Libros();
                    break;
                case 8:
                    busquedaAutorNombre();
                    break;
                case 0:
                    System.out.println("\nCerrando la aplicación .......");
                    break;
                default:
                    System.out.println("\nIntroduzca una opción válida!");
            }
        }
    }

    private String verLibro(Libro l) {
        String autores = "", idiomas = "";
        for (Autor autor : l.getAutores())
            autores += "\n\t" + autor.getNombre();
        for (Idioma idioma : l.getIdiomas())
            idiomas += "\n\t" + idioma;
        return "============= *** LIBRO *** =============\n" +
                "Titulo: " + l.getTitulo() + "\n" +
                "Autores: " + l.getAutores().size() +
                autores + "\n" +
                "Idiomas: " +
                idiomas + "\n" +
                "Descargas: " + l.getDescargas() + "\n" +
                "=========================================";
    }

    private String verAutor(Autor a) {
        String libros = "";
        for (Libro libro : a.getLibros())
            libros += "\n\t" + libro.getTitulo();
        return "============= *** AUTOR *** ==============\n" +
                "Nombre: " + a.getNombre() + "\n" +
                "Año de nacimiento: " + a.getFechaDeNacimiento() + "\n" +
                "Año de fallecimiento: " + a.getFechaDeFallecimiento() + "\n" +
                "Libros: " + a.getLibros().size() +
                libros + "\n" +
                "=========================================";
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
                        Autor autorExistente = autorRepository.autorPorNombre(a.nombre());
                        if (autorExistente != null) {
                            return autorExistente; // Si ya existe, usar el autor encontrado en la base de datos
                        } else {
                            return autorRepository.save(new Autor(a.nombre(), a.fechaDeNacimiento(), a.fechaDeFallecimiento()));
                        }
                    })
                    .collect(Collectors.toSet());
            libroRepository.save(new Libro(datosLibro.titulo(), autor, new HashSet<>(datosLibro.idiomas()), datosLibro.descargas()));
            Libro l = libroRepository.findByTitulo(datosLibro.titulo());
            System.out.println(verLibro(l));
        } else
            System.out.println("Libro no encontrado");
    }

    private void listarLibrosRegistrados() {
        List<Libro> listaLibros = libroRepository.busquedaLibros();
        listaLibros.forEach(l -> {
            System.out.println(verLibro(l));
        });
        System.out.println("\n");
    }

    private void listarAutoresRegistrados() {
        List<Autor> listarAutores = autorRepository.listaAutor();
        listarAutores.forEach(a -> {
            System.out.println(verAutor(a));
        });
        System.out.println("\n");
    }

    private void listarAutoresVivosDeterminadoAnio() {
        System.out.println("Introduzca el año en el que desea buscar para ver autor(es) que estaban vivos");
        int anio = teclado.nextInt();
        teclado.nextLine();
        List<Autor> listarAutores = autorRepository.autorVivoPorAnio(anio);
        if (!listarAutores.isEmpty()) {
            listarAutores.forEach(a -> {
                System.out.println(verAutor(a));
            });
            System.out.println("\n");
        } else
            System.out.println("No se encontro el autor que vivio en " + anio);
    }

    private void listaDeLibrosPorIdiomas() {
        int[] index = {1};
        System.out.println("Introduzca un idioma que desee buscar " +
                "(es - español , en - inglés,  fr - francés, pt - protugés)");
        String lenguaje = teclado.nextLine().toLowerCase();
        Idioma idioma = Idioma.fromString(lenguaje);
        List<Libro> listaLibros = libroRepository.busquedaLibrosPorIdioma(idioma);
        if (!listaLibros.isEmpty()) {
            System.out.println("Idioma: " + idioma + " - Cantidad de libros: " + listaLibros.size());
            listaLibros.forEach(l -> {
                System.out.println("\t\t\t\t\t(" + index[0] + ")");
                System.out.println(verLibro(l));
                index[0]++;
            });
            System.out.println("\n");
        } else
            System.out.println("No se encontro libros con el idioma " + lenguaje);
    }

    private void estadisticas() {
        List<Libro> datosLibros = libroRepository.busquedaLibros();
        DoubleSummaryStatistics est = datosLibros.stream()
                .filter(l -> l.getDescargas() > 0)
                .collect(Collectors.summarizingDouble(Libro::getDescargas));
        Libro libro = new Libro();
        System.out.println("============ *** ESTADISTICAS *** =========");
        System.out.println("Media de las descargas: " + est.getAverage());
        System.out.println("===========================================");
        datosLibros.forEach(l -> {
            if (l.getDescargas() == est.getMax()) {
                System.out.println("Libro mas descargado: " + est.getMax());
                System.out.println(verLibro(l));
            } else if (l.getDescargas() == est.getMin()) {
                System.out.println("Libro menos descargado:  " + est.getMin());
                System.out.println(verLibro(l));
            }
        });
        System.out.println("\n");
    }

    private void top10Libros() {
        int[] index = {1};
        List<Libro> listaLibro = libroRepository.findTop10ByOrderByDescargasDesc();
        System.out.println(listaLibro.size());
        listaLibro.forEach(l -> {
            System.out.println("\t\t\t\t(Puesto " + index[0] + ")");
            System.out.println(verLibro(l));
            index[0]++;
        });
        System.out.println("\n");
    }

    private void busquedaAutorNombre() {
        System.out.println("Intraduzca el nombre del autor que desee buscar");
        String nombre = teclado.nextLine();
        Autor autor = autorRepository.autorPorNombre(nombre.toLowerCase());
        if (autor.getNombre() == nombre) {
            System.out.println(verAutor(autor));
            System.out.println("\n");
        } else
            System.out.println("No se encontro autor con el nombre: " + nombre);
    }
}

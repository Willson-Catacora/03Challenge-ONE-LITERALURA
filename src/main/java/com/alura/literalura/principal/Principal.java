package com.alura.literalura.principal;

import com.alura.literalura.model.Datos;
import com.alura.literalura.service.ConsumirAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumirAPI consumirApi = new ConsumirAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();

    private final String URL_BASE = "https://gutendex.com/books/";
    private Integer opcion = -1;

    public void mostrarMenu() {
        while (opcion != 0) {
            var menu = """
                    ***===============================================***
                    \t\tElija la opción a travéz  de su número
                    ***===============================================***
                    \t1 - buscar libro por título
                    \t2 - listar libros registrados
                    \t3 - listar autores registrados
                    \t4 - listar autores vivos en determinado año
                    \t5 - listar libros por idioma
                    
                    \t0  - Salir
                    ***===============================================***
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
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
        var libro = teclado.nextLine();
        var json = consumirApi.obtenerDatos(URL_BASE +"?search="+ libro.toLowerCase().replace(" ","%20"));
        var datos = convierteDatos.obtenerDatos(json, Datos.class);
        System.out.println(datos);
    }
}

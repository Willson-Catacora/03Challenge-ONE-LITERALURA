<h1 align="center"> ONE - Challenge Literalura </h1>

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=black)

![Proyecto Terminado](https://img.shields.io/badge/Estado-Proyecto%20Terminado-brightgreen)

## Descripción del Proyecto ##
Literalura es un sistema de gestión de libros y autores desarrollado con Spring Boot, JPA y PostgreSQL. Este proyecto proporciona una interfaz 
interactiva para realizar diversas operaciones relacionadas con la gestión de una biblioteca digital, incluyendo búsqueda de libros, registro de autores, y más.

## Configuracion del proyecto ##

Clonar el Repositorio

### Configuración de la Base de Datos ###
1. Si usas Docker
  - Variables de entorno
      Para la ejecucion del proyecto se tiene que crear el archivo de .env para poner las variables de entorno o crear dichas variables
      en las variables de entorno de su equipo (windows).
  
      ejemplo (archivo .env)
  
    ![image](https://github.com/user-attachments/assets/7fe1363f-5ac0-47b1-a075-98ef14384e3f)
    
  - Configuracion del archivo src/main/resources/application.properties (por defecto si clonas este repositorio)
    ```bash
      spring.application.name=literalura
      spring.datasource.url=jdbc:postgresql://${DB_HOST}:${PORT}/${DB}
      spring.datasource.username=${DB_USER}
      spring.datasource.password=${DB_PASSWORD}
      ```
  - Ejecutar docker-compose en el cmd (en la raiz del proyecto)
    ```bash
     docker-compose up -d
    ```

2.  Configurar la Base de Datos (si tienes instalado no usar docker)

  - Crea una base de datos en PostgreSQL para el proyecto.
  
  - Actualiza el archivo src/main/resources/application.properties con tus credenciales de base de datos:
      ```bash
      spring.application.name=literalura
      spring.datasource.url=jdbc:postgresql://${DB_HOST}/${DB}
      spring.datasource.username=${DB_USER}
      spring.datasource.password=${DB_PASSWORD}
      ```
### API ###
Los datos que procesa este proyecto son de la API Gutendex que es gratuita, para lo cual se recomienda ver la documentacion de la api y familiarizarse con los datos que
nos proporciona esta.

 https://gutendex.com/


## Funcionalidad del Proyecto ##

El proyecto obtiene datos de la api https://gutendex.com/ en formato JSON, de la cual procesaremos para luego guardar en la base de datos para asi poder hacer la funcionalidad:
- buscar libro por título
- listar libros registrados
- listar autores registrados
- listar autores vivos en determinado año
- listar libros por idioma
(Extra)
- generar estadisticas
- top 10 libros mas descargados (libros registrados)
- buscar autor por nombre


![image](https://github.com/user-attachments/assets/2177fd33-272b-48a8-8670-3005f4797531)



##  Tecnologías Utilizadas ##
- JAVA
- Framework -> Spring Boot
- PostgreSQL
- Docker

## Autor ##
willson catacora valencia

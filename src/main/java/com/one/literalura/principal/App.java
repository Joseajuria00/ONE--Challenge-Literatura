package com.one.literalura.principal;

import com.one.literalura.model.Author;
import com.one.literalura.model.Book;
import com.one.literalura.model.Language;
import com.one.literalura.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class App {

    private Scanner keyboard = new Scanner(System.in);
    private BookService bookService;
    @Autowired
    public App(BookService bookService) {
        this.bookService = bookService;;
    }

    public void Menu() {
        var choice = -1;
        while (choice != 0) {
            var menu = """
                    ::::::::::::::::: LiterAlura :::::::::::::::::
                    1 - Buscar libro por titulo
                    2 - Buscar autores por nombre
                    3 - Listar libros registrados
                    4 - Listar autores registrados
                    5 - Listar autores vivos en un determinado año
                    6 - Listar libros por idioma
                    7 - Top 5 libros mas descargados
                    8 - Estadísticas de descargas
                    0 - Salir
                    ::::::::::::::::::::::::::::::::::::::::::::::
                    Elige una opcion:""";
            System.out.println("\n" + menu);
            choice = keyboard.nextInt();
            keyboard.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("-- BUSCAR LIBRO POR TITULO --");
                    findBookByTitle();
                    break;
                case 2:
                    System.out.println("-- BUSCAR AUTOR POR NOMBRE --");
                    findAuthorByName();
                    break;
                case 3:
                    System.out.println("-- MOSTRAR LIBROS REGISTRADOS --");
                    findAllRegisteredBooks();
                    break;
                case 4:
                    System.out.println("-- MOSTRAR AUTORES REGISTRADOS --");
                    findAllRegisteredAuthors();
                    break;
                case 5:
                    System.out.println("-- BUSCADOR DE AUTORES VIVOS POR AÑO --");
                    findLivingAuthorsByYear();
                    break;
                case 6:
                    System.out.println("-- BUSCADOR DE LIBROS POR IDIOMA --");
                    findBookPerLanguage();
                    break;
                case 7:
                    System.out.println("-- TOP 5 LIBROS MÁS DESCARGADOS --");
                    topBookPerDownload();
                    break;
                case 8:
                    System.out.println("-- ESTADÍSTICAS--");
                    summaryStatsPerDownload();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void findBookByTitle(){
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var titleBook = keyboard.nextLine();
        Optional<Book> book = bookService.findBookByTitle(titleBook);
        if(book.isPresent()){
            System.out.println(book.get());
        } else {
            System.out.println("\nLibro no encontrado");
        }
    }
    private void findAuthorByName() {
        System.out.println("Ingrese el nombre del autor que desea buscar:");
        var nameAuthor = keyboard.nextLine();
        Optional<Author> author = bookService.findAuthorByName(nameAuthor);
        if(author.isPresent()){
            System.out.println(author.get().toStringSinLibros());
        } else {
            System.out.println("\nAutor no encontrado");
        }
    }
    private void findAllRegisteredBooks() {
        List<Book> books = bookService.findAllRegisteredBooks();
        if(!books.isEmpty()){
            books.stream().sorted(Comparator.comparing(Book::getDownloads).reversed()).forEach(System.out::println);
        } else {
            System.out.println("No se han encontrado libros");
        }
    }
    private void findAllRegisteredAuthors() {
        List<Author> autores = bookService.findAllRegisteredAuthors();
        if(!autores.isEmpty()){
            autores.stream()
                    .sorted(Comparator.comparing(Author::getBirthYear,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .forEach(System.out::println);
        } else {
            System.out.println("No se han encontrado autores");
        }
    }
    private void findLivingAuthorsByYear() {
        System.out.println("Ingrese el año para buscar autores vivos en esa fecha: ");
        Integer year = Integer.parseInt(keyboard.nextLine());
        List<Author> autores = bookService.findLivingAuthorsByYear(year);
        if(!autores.isEmpty()){
            autores.forEach(System.out::println);
        } else {
            System.out.println("No se han registrado autores vivos en ese año");
        }
    }
    private void findBookPerLanguage() {
        var languages = this.bookService.findLanguagesInDataBase();
        if (languages.isEmpty()) {
            System.out.println("No hay idiomas disponibles en la base de datos.");
            return;
        }
        System.out.println("Idiomas disponibles: ");
        languages.forEach(lan -> System.out.println(Language.getLanguageMap().getOrDefault(lan, lan)));

        System.out.println("Ingrese el idioma (abreviación o nombre):");
        var lan = keyboard.nextLine();
        String validLanguage = bookService.obtainValidLanguage(languages, lan);
        if(validLanguage!=null) this.bookService.findBookPerLanguage(validLanguage);
        else System.out.println("\nIdioma no válido. Intente nuevamente.");
    }
    private void topBookPerDownload(){
        this.bookService.topBookPerDownloadCount();
    }
    private void summaryStatsPerDownload(){
        this.bookService.summaryStatsPerDownload();
    }
}

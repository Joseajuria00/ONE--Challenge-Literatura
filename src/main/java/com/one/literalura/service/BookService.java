package com.one.literalura.service;

import com.one.literalura.model.Author;
import com.one.literalura.model.Book;
import com.one.literalura.model.Language;
import com.one.literalura.model.records.DataAuthor;
import com.one.literalura.model.records.DataBook;
import com.one.literalura.model.records.Results;
import com.one.literalura.repository.AuthorRepository;
import com.one.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private APIConsumer apiConsumer = new APIConsumer();
    private final String URL_BASE = "https://gutendex.com/books/";
    private DataConverter conversor = new DataConverter();
    @Autowired
    public BookService(BookRepository book, AuthorRepository author) {
        this.bookRepository = book;
        this.authorRepository = author;
    }

    public void addBookToDataBase(Book book) {
        if (bookRepository.findByTitleContainsIgnoreCase(book.getTitle()).isEmpty()) {
            bookRepository.save(book);
        } else {
            System.out.println("\nNo se puede registrar el mismo libro más de una vez");
        }
    }
    public void addAuthorToDataBase(Author author) {
        if (authorRepository.findByNameContainsIgnoreCase(author.getName()).isEmpty()) {
            authorRepository.save(author);
        } else {
            System.out.println("\nNo se puede registrar el mismo autor más de una vez");
        }
    }
    public Optional<Book> findBookByTitle(String titleBook) {
        var jsonResponse = apiConsumer.obtenerDatos(URL_BASE + "?search=" + titleBook.replace(" ", "+"));
        //System.out.println(jsonResponse);
        Results results = conversor.obtenerDatos(jsonResponse, Results.class);
        DataBook dataBook1 = results.results().isEmpty()?null:results.results().get(0);
        Optional<DataBook> dataBook = results.results().stream().findFirst();
        if(dataBook.isPresent()){
            //System.out.println(dataBook);
            Book book = new Book(dataBook.get());
            List<Author> persistedAuthors = book.getAuthors().stream()
                    .map(a -> authorRepository.getByName(a.getName())
                            .orElseGet(() -> authorRepository.save(a)))
                    .toList();
            book.setAuthors(persistedAuthors);

            // Guardar book si no existe
            addBookToDataBase(book);
            return Optional.of(book);
        }
        return Optional.empty();
    }
    public Optional<Author> findAuthorByName(String nameAuthor) {
        var jsonResponse = apiConsumer.obtenerDatos(URL_BASE + "?search=" + nameAuthor.replace(" ", "+"));
        //System.out.println(jsonResponse);
        Results results = conversor.obtenerDatos(jsonResponse, Results.class);
        Optional<DataBook> dataBook = results.results().stream().findFirst();
        if(dataBook.isPresent()){
            //System.out.println(dataBook);
            List<DataAuthor> dataAuthorList = dataBook.get().authors();
            Optional<DataAuthor> dataAuthor =
                    dataAuthorList.stream().findFirst();
            if(dataAuthor.isPresent()){
                Author author = new Author(dataAuthor.get());
                //Guardar author si no existe
                addAuthorToDataBase(author);
                return Optional.of(author);
            }

        }
        return Optional.empty();
    }
    public List<Book> findAllRegisteredBooks(){
        return bookRepository.findAll();
    }
    public List<Author> findAllRegisteredAuthors(){
        return authorRepository.findAll();
    }
    public List<Author> findLivingAuthorsByYear(Integer year) {
        return authorRepository.findLivingAuthorsByYear(year);
    }
    public void findBookPerLanguage(String lan) {
        var bookList = this.bookRepository.findBooksByLanguage(lan);
        if (!bookList.isEmpty()) {
            bookList.forEach(System.out::println);
        } else {
            System.out.println("\nNo se han encontrado libros con el idioma: " + lan + "\n");
        }
    }
    public String obtainValidLanguage(Set<String> languages, String lan){
        if (lan == null || lan.isBlank()) {
            return null;
        }
        String lanAbbreviation;
        try {
            // Si se ingresa el idioma con su nombre completo, se obtiene su abreviación
            lanAbbreviation = Language.fromSpanish(lan).getLanguageAbbreviation();
        } catch (IllegalArgumentException e) {
            // Si no coincide con ningún idioma en español, usar tal cual como fue ingresado
            lanAbbreviation = lan;
        }
        return languages.contains(lanAbbreviation)?lanAbbreviation:null;
    }
    public Set<String> findLanguagesInDataBase() {
        return new HashSet<>(this.bookRepository.findLanguagesInDataBase());
    }
    public void topBookPerDownloadCount() {
        List<Book> listTop = this.bookRepository.findAll();
        listTop.stream().sorted(Comparator.comparing(Book::getDownloads).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
    public void summaryStatsPerDownload() {
        List<Book> bookList = this.bookRepository.findAll();
        IntSummaryStatistics summaryStats = bookList.stream().filter(f -> f.getDownloads() > 0)
                .collect(Collectors.summarizingInt(Book::getDownloads));

        String summaryStatsString = String.format("""
                        
                        --------------- Stats ---------------
                        - Media de descargas por libro: %.1f
                        - Cantidad minima de descargas: %.1f
                        - Cantidad maxima de descargas: %.1f
                        - Total de descargas: %.1f
                        - Total de libros contados: %.1f
                        -------------------------------------""",
                Double.valueOf(summaryStats.getAverage()),
                Double.valueOf(summaryStats.getMin()),
                Double.valueOf(summaryStats.getMax()),
                Double.valueOf(summaryStats.getSum()),
                Double.valueOf(summaryStats.getCount()));

        System.out.println(summaryStatsString);
    }
}

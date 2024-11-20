package com.one.literalura.model;

import com.one.literalura.model.records.DataBook;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String title;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id")
    )
    private List<Author> authors;
    private List<String> languages;
    private Integer downloads;

    public Book(DataBook dataBook) {
        this.title = dataBook.title();
        this.authors = dataBook.authors().stream().map(Author::new).collect(Collectors.toList());
        this.languages = dataBook.languages();
        this.downloads = dataBook.downloads();
    }

    @Override
    public String toString() {
        String autoresString = authors == null || authors.isEmpty()
                ? "Sin autores registrados"
                : authors.stream().map(Author::toStringSinLibros).collect(Collectors.joining(", "));
        return String.format("""
                \n--------- LIBRO ---------
                - Titulo: %s
                - Autores: %s
                - Idiomas: %s
                - Descargas: %s
                -------------------------""", title, autoresString, (languages == null|| languages.isEmpty())?
                "Sin idiomas registrados":languages, downloads);
    }
}

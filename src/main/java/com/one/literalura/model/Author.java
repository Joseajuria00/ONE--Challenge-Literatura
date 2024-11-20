package com.one.literalura.model;

import com.one.literalura.model.records.DataAuthor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

import static com.one.literalura.service.Utilities.reversedName;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private List<Book> books;
    public Author(DataAuthor autor) {
        this.name = autor.name();
        this.birthYear = autor.birthYear();
        this.deathYear = autor.deathYear();
    }

    @Override
    public String toString() {
        String librosString = books == null || books.isEmpty()
                ? "Sin libros registrados"
                : books.stream().map(l->"["+l.getTitle()+"]").collect(Collectors.joining(", "));
        return String.format("""
                \n--------- Autor ---------
                - Autor: %s
                - Año de Nacimiento: %s
                - Año de Fallecimiento: %s
                - Libros: %s
                -------------------------""", reversedName(name), birthYear, deathYear, librosString);
    }
    public String toStringSinLibros(){
        return reversedName(name) +
                " (" + birthYear +
                "-" + deathYear + ")";
    }
}

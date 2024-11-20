package com.one.literalura.repository;

import com.one.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    //Boolean existsByTitleIgnoreCase(String title);
    List<Book> findByTitleContainsIgnoreCase(String title);
    //List<Libro> findLibrosByLanguage(String language);
    @Query(value = "SELECT unnest(languages) AS language FROM books GROUP BY language", nativeQuery = true)
    List<String> findLanguagesInDataBase();
    @Query(value = "SELECT * FROM books b WHERE :language = ANY(b.languages)", nativeQuery = true)
    List<Book> findBooksByLanguage(@Param("language") String language);
}

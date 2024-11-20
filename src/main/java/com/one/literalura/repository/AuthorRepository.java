package com.one.literalura.repository;

import com.one.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameContainsIgnoreCase(String name);
    Optional<Author> getByName(String name);

    @Query("select a from Author a where a.birthYear <= :year and a.deathYear > :year")
    List<Author> findLivingAuthorsByYear(Integer year);
}

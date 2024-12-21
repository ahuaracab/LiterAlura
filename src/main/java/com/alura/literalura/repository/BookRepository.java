package com.alura.literalura.repository;

import com.alura.literalura.model.Book;
import com.alura.literalura.model.Language;
import com.alura.literalura.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {

    Set<Book> findByTitleContainingIgnoreCase(String title);

    @NonNull
    List<Book> findAll();

    @Query("SELECT p FROM Book b JOIN b.authors p WHERE p.deathYear IS NULL OR p.deathYear > :year")
    Set<Person> findAuthorsAliveInYear(Integer year);

    @Query("SELECT b FROM Book b WHERE b.language = :language")
    Set<Book> findBooksByLanguage(@Param("language") Language language);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    Set<Book> findBooksByAuthor(@Param("authorId") Long authorId);
}

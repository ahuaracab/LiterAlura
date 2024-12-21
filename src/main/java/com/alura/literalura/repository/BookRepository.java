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

    // 1. Buscar libro por título
    Set<Book> findByTitleContainingIgnoreCase(String title);

    // 2. Listar libros registrados (esto es proporcionado por defecto por JpaRepository)
    @NonNull
    List<Book> findAll();

    // 3. Listar autores registrados
    @Query("SELECT p FROM Book b JOIN b.authors p")
    Set<Person> findAllAuthors();

    // 4. Listar autores vivos en un determinado año
    @Query("SELECT p FROM Book b JOIN b.authors p WHERE p.deathYear IS NULL OR p.deathYear > :year")
    Set<Person> findAuthorsAliveInYear(Integer year);

    // 5. Listar libros por idioma
    @Query("SELECT b FROM Book b WHERE b.language = :language")
    Set<Book> findBooksByLanguage(@Param("language") Language language);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    Set<Book> findBooksByAuthor(@Param("authorId") Long authorId);
}

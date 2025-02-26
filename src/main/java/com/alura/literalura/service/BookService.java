package com.alura.literalura.service;

import com.alura.literalura.model.Book;
import com.alura.literalura.model.Language;
import com.alura.literalura.model.Person;
import com.alura.literalura.repository.BookRepository;
import com.alura.literalura.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    @Autowired
    private PersonRepository personRepository;

    public Optional<Book> searchByTitle(String title) {
        return repository.findByTitleContainingIgnoreCase(title).stream().findFirst();
    }

    public void saveBook(Book book) {
        savePersonsIfNotExist(book.getAuthors());
        savePersonsIfNotExist(book.getTranslators());
        repository.save(book);
    }

    private void savePersonsIfNotExist(Set<Person> persons) {
        for (Person person : persons) {
            if (person.getId() == null) {
                personRepository.save(person);
            }
        }
    }

    public Set<Book> listAllRegisteredBooks() {
        List<Book> books = repository.findAll();
        return new HashSet<>(books);
    }

    public Set<Person> listAuthorsAliveInYear(int year) {
        return repository.findAuthorsAliveInYear(year);
    }

    public Set<Book> listBooksByLanguage(String language) {
        Language langEnum = Language.fromString(language);
        return repository.findBooksByLanguage(langEnum);
    }

    public Set<Book> listBooksByAuthor(Long authorId) {
        return repository.findBooksByAuthor(authorId);
    }
}

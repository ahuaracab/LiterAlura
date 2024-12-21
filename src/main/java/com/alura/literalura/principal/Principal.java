package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.service.ApiService;
import com.alura.literalura.service.BookService;
import com.alura.literalura.service.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final ApiService apiService = new ApiService();
    private final DataConverter dataConverter = new DataConverter();

    @Autowired
    private BookService bookService;

    public Principal(BookService bookService) {
        this.bookService = bookService;
    }

    public void menu() {
        int option = -1;
        while (option != 0) {
            System.out.println("\nMENU PRINCIPAL\n");
            var menu = """
                    1- Buscar libro por título
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            try {
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        searchByTitle();
                        break;
                    case 2:
                        listAllRegisteredBooks();
                        break;
                    case 3:
                        listAllRegisteredAuthors();
                        break;
                    case 4:
                        listAuthorsAliveInYear();
                        break;
                    case 5:
                        listBooksByLanguage();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }
    }

    private void searchByTitle() {
        System.out.println("\nIngrese el título del libro que desea buscar:");
        String title = scanner.nextLine();
        String URL_BASE = "https://gutendex.com/books/?search=";
        String json = apiService.fetchData(URL_BASE + title.replace(" ", "%20"));

        try {
            ApiResponse apiResponse = dataConverter.fetchData(json, ApiResponse.class);
            List<BookData> bookDataList = apiResponse.getResults();

            if (bookDataList == null || bookDataList.isEmpty()) {
                System.out.println("\nNo se encontró ningún libro con ese título.\n");
            } else {
                BookData bookData = bookDataList.getFirst();
                Book book = new Book(bookData);

                if (bookService.searchByTitle(book.getTitle()).isPresent()) {
                    System.out.println("\nEl libro ya existe en la base de datos: " + book.getTitle() + "\n");
                } else {
                    bookService.saveBook(book);
                    System.out.println("\n******** LIBRO REGISTRADO ********\n");
                    showBookDetails(book);
                }
            }
        } catch (Exception e) {
            System.out.println("\nError al procesar los datos de la API: " + e.getMessage() + "\n");
        }
    }

    private void listAllRegisteredBooks() {
        Set<Book> books = bookService.listAllRegisteredBooks();
        System.out.println("\n******** LIBROS REGISTRADOS ********\n");
        int i = 1;
        for (Book book : books) {
            System.out.println("Libro " + i++ + ":");
            showBookDetails(book);
        }
    }

    private void listAllRegisteredAuthors() {
        Set<Book> books = bookService.listAllRegisteredBooks();
        Map<String, Person> authorMap = new HashMap<>();
        Map<String, Set<String>> authorBooksMap = new HashMap<>();

        for (Book book : books) {
            for (Person author : book.getAuthors()) {
                authorMap.put(author.getName(), author);
                authorBooksMap
                        .computeIfAbsent(author.getName(), k -> new HashSet<>())
                        .add(book.getTitle());
            }
        }

        System.out.println("\n******** AUTORES REGISTRADOS ********\n");
        for (Map.Entry<String, Person> entry : authorMap.entrySet()) {
            Person author = entry.getValue();
            Set<String> bookTitles = authorBooksMap.get(entry.getKey());

            System.out.println("Nombre: " + author.getName());
            System.out.println("Fecha de nacimiento: " + author.getBirthYear());
            System.out.println("Fecha de fallecimiento: " + (author.getDeathYear() != null ? author.getDeathYear() : "N/A"));
            System.out.println("Libros escritos: [" + String.join(", ", bookTitles) + "]");
            System.out.println("**********************************\n");
        }
    }

    private void listAuthorsAliveInYear() {
        System.out.println("\nIngrese el año para listar autores vivos:");
        int year = scanner.nextInt();
        scanner.nextLine();  // Limpiar el buffer
        Set<Person> authors = bookService.listAuthorsAliveInYear(year);

        Map<String, Set<String>> authorBooksMap = new HashMap<>();

        for (Person author : authors) {
            authorBooksMap.put(author.getName(), bookService.listBooksByAuthor(author.getId())
                    .stream()
                    .map(Book::getTitle)
                    .collect(Collectors.toSet()));
        }

        System.out.println("\nAUTORES VIVOS EN EL AÑO " + year + ":\n");
        for (Person author : authors) {
            System.out.println("Nombre: " + author.getName());
            System.out.println("Fecha de nacimiento: " + author.getBirthYear());
            System.out.println("Fecha de fallecimiento: " + (author.getDeathYear() != null ? author.getDeathYear() : "N/A"));
            String bookTitles = String.join(", ", authorBooksMap.get(author.getName()));
            System.out.println("Libros escritos: [" + bookTitles + "]");
            System.out.println("**********************************\n");
        }
    }

    private void listBooksByLanguage() {
        System.out.println("\n******** IDIOMAS DISPONIBLES ********\n");
        for (Language language : Language.values()) {
            System.out.println(language.name() + " - " + language.getFullName());
        }
        System.out.println("\n************************************\n");

        System.out.println("Ingrese el idioma para listar libros:");
        String languageInput = scanner.nextLine();
        try {
            Language language = Language.fromString(languageInput);
            Set<Book> booksByLanguage = bookService.listBooksByLanguage(language.name());
            if (booksByLanguage.isEmpty()) {
                System.out.println("\nNo se encontraron libros en el idioma: " + language.getFullName() + "\n");
            } else {
                System.out.println("\n******** LIBROS EN IDIOMA: " + language.getFullName().toUpperCase() + " ********\n");
                for (Book book : booksByLanguage) {
                    System.out.println("Título: " + book.getTitle());
                    System.out.println("Autor: " + (book.getAuthors().isEmpty() ? "Desconocido" : book.getAuthors().iterator().next().getName()));
                    System.out.println("Idioma: " + book.getLanguage().getFullName());
                    System.out.println("Número de descargas: " + book.getDownloadCount());
                    System.out.println("**********************************\n");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nIdioma no reconocido. Por favor, ingrese un idioma válido.\n");
        }
        System.out.println();
    }


    private void showBookDetails(Book book) {
        System.out.println("**********************************");
        System.out.println("Título: " + book.getTitle());
        System.out.println("Autor: " + (book.getAuthors().isEmpty() ? "Desconocido" : book.getAuthors().iterator().next().getName()));
        System.out.println("Idioma: " + book.getLanguage().getFullName());
        System.out.println("Número de descargas: " + book.getDownloadCount());
        System.out.println("**********************************\n");
    }
}

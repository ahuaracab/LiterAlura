package com.alura.literalura.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> subjects = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Person> authors = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Person> translators = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> bookshelves = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Language language;

    private Boolean copyright;

    @Column(name = "media_type")
    private String mediaType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_formats", joinColumns = @JoinColumn(name = "book_id"))
    @MapKeyColumn(name = "format_type")
    @Column(name = "format_url")
    private Map<String, String> formats = new HashMap<>();

    @Column(name = "download_count")
    private Integer downloadCount;

    public Book() {}

    public Book(BookData bookData) {
        this.title = bookData.title();
        this.subjects = bookData.subjects();
        this.authors = convertPersonDataToPersons(bookData.authors());
        this.translators = convertPersonDataToPersons(bookData.translators());
        this.bookshelves = bookData.bookshelves();
        this.language = bookData.getPrimaryLanguage();
        this.copyright = bookData.copyright();
        this.mediaType = bookData.mediaType();
        this.formats = bookData.formats();
        this.downloadCount = bookData.downloadCount();
    }

    private Set<Person> convertPersonDataToPersons(Set<PersonData> personDataList) {
        Set<Person> persons = new HashSet<>();
        for (PersonData personData : personDataList) {
            Person person = new Person();
            person.setName(personData.name());
            person.setBirthYear(personData.birthYear());
            person.setDeathYear(personData.deathYear());
            persons.add(person);
        }
        return persons;
    }

    public Set<Person> getTranslators() {
        return translators;
    }

    public void setTranslators(Set<Person> translators) {
        this.translators = translators;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<String> subjects) {
        this.subjects = subjects;
    }

    public Set<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Person> authors) {
        this.authors = authors;
    }

    public Set<String> getBookshelves() {
        return bookshelves;
    }

    public void setBookshelves(Set<String> bookshelves) {
        this.bookshelves = bookshelves;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Boolean getCopyright() {
        return copyright;
    }

    public void setCopyright(Boolean copyright) {
        this.copyright = copyright;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Map<String, String> getFormats() {
        return formats;
    }

    public void setFormats(Map<String, String> formats) {
        this.formats = formats;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }
}

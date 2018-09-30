package com.microservice.entity;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "book")
public class Book implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;
    private String publisher;
    private int numPages;
    private String isbn;
    @NotNull
    private String plot;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
            })
    @JoinTable(name = "book_authors",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "author_id") })
    @JsonIgnore
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE
            })
    @JoinTable(name = "book_genres",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "genre_id") })
    @JsonIgnore
    private Set<Genre> genres = new HashSet<>();
    
    @OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, optional = true)
    @JoinColumn(name = "file_id", nullable = true)
    @JsonIgnore
    private DBFile cover;
    
    // Constructors
    public Book() {
    }

    public Book(String title, String publisher, int numPages, String isbn, String plot) {
        this.title = title;
        this.publisher = publisher;
        this.numPages = numPages;
        this.isbn = isbn;
        this.plot = plot;
    }

    // Getters and setters  

    public DBFile getCover() {
        return cover;
    }

    public void setCover(DBFile cover) {
        this.cover = cover;
    }
    
    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {    
        this.genres = genres;
    }

    public Set<Author> getAuthors() {
        return authors;
    }
        
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
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

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

}

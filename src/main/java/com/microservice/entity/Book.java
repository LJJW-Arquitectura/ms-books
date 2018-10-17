package com.microservice.entity;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import javax.persistence.*;
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
    private String plot;
    private String[] authors;
    private String[] genres;
    
    @OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, optional = true)
    @JoinColumn(name = "file_id", nullable = true)
    @JsonIgnore
    private DBFile cover;
    
    // Constructors
    public Book() {
    }

    public Book(Long id, String title, String publisher, int numPages, String isbn, String plot, String[] authors, String[] genres) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.numPages = numPages;
        this.isbn = isbn;
        this.plot = plot;
        this.authors = authors;
        this.genres = genres;
    }

    // Getters and setters  
    public DBFile getCover() {
        return cover;
    }

    public void setCover(DBFile cover) {
        this.cover = cover;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
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

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

}

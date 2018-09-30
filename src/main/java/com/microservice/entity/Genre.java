package com.microservice.entity;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "genre")
public class Genre implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String name;
    @NotNull
    private String description;

    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        },
        mappedBy = "genres")
    @JsonIgnore
    private Set<Book> books = new HashSet<>();

    // Constructors
    public Genre() {
    }

    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
    
}

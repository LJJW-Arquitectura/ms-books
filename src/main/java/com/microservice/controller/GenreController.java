package com.microservice.controller;

import com.microservice.entity.*;
import com.microservice.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class GenreController {

    @Autowired
    private GenreRepository genreRepository;

    /*@POST*/
    // Post genre
    @PostMapping("/genres")
    public Genre create(@RequestBody Genre genre)
    {
        return genreRepository.save(genre);
    }



    /*@GET*/
    // Get all genres
    @GetMapping("/genres")
    public List<Genre> findAll()
    {
        return genreRepository.findAll();
    }

    // Get genre by ID
    @GetMapping("/genres/{genre_id}")
    @ResponseBody
    public Genre findByGenreId(@PathVariable("genre_id") Long genreId)
    {
        return genreRepository.findOne(genreId);
    }

    // Get genre by name
    @GetMapping("/genre/name/{genre_name}")
    public List<Genre> findByGenreName(@PathVariable("genre_name") String genreName)
    {
        return genreRepository.findByName(genreName);
    }

    // Get books of a gender by ID
    @GetMapping("/genres/{genre_id}/books")
    @ResponseBody
    public Set<Book> findBooksByGenreId(@PathVariable("genre_id") Long genreId) 
    {
        Genre genre = genreRepository.findOne(genreId);
        return genre.getBooks();
    }



    /*@PUT*/
    // Put genre by id
    @PutMapping("/genres/{genre_id}")
    public Genre update(@PathVariable("genre_id") Long genreId, @RequestBody Genre genreObject)
    {
        Genre genre = genreRepository.findOne(genreId);
        genre.setName(genreObject.getName());
        return genreRepository.save(genre);
    }



    /*@DELETE*/
    // Delete genre by id
    @DeleteMapping("/genres/{genre_id}")
    public List<Genre> delete(@PathVariable("genre_id") Long genreId)
    {
        genreRepository.delete(genreId);
        return genreRepository.findAll();
    }

}

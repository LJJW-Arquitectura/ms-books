package com.microservice.controller;

import com.microservice.entity.*;
import com.microservice.repository.GenreRepository;
import com.microservice.response.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/books-ms/genres")
public class GenreController {

    @Autowired
    private GenreRepository genreRepository;

    /*@POST*/
    // Post genre
    @PostMapping("")
    public ResponseEntity createGenre(@RequestBody Genre genre)
    {
        genreRepository.save(genre);
        return new ResponseEntity(HttpStatus.CREATED);
    }



    /*@GET*/
    // Get all genres
    @GetMapping("")
    public ResponseEntity findAllGenres()
    {
        List<Genre> genres = genreRepository.findAll();
        return new ResponseEntity(genres, HttpStatus.OK);
    }

    // Get genre by ID
    @GetMapping("/{genre_id}")
    public ResponseEntity findByGenreId(@PathVariable("genre_id") Long genreId)
    {
        Genre genre = genreRepository.findOne(genreId);
        if (genre == null) {
            return new ResponseEntity("Genre with id " + genreId + " not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(genre, HttpStatus.OK);
    }
    
    // Get genre by name
    @GetMapping("/name/{genre_name}")
    public ResponseEntity findByGenreName(@PathVariable("genre_name") String genreName)
    {
        List<Genre> genres = genreRepository.findByName(genreName);
        return new ResponseEntity(genres, HttpStatus.OK);
    }

    // Get books of a genre by ID
    @GetMapping("/{genre_id}/books")
    @ResponseBody
    public ResponseEntity findBooksOfGenre(@PathVariable("genre_id") Long genreId) 
    {
        Genre genre = genreRepository.findOne(genreId);
        if (genre == null) {
            return new ResponseEntity("Genre with id " + genreId + " not found", HttpStatus.NOT_FOUND);
        }
        
        Set<Book> books = genre.getBooks();
        List<BookResponse> response = new ArrayList<>();
        for (Book book : books) {
            response.add(new BookResponse(book));
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }




    
    /*@PUT*/
    // Put genre by id
    @PutMapping("/{genre_id}")
    public ResponseEntity updateGenre(@PathVariable("genre_id") Long genreId, @RequestBody Genre genreObject)
    {
        Genre genre = genreRepository.findOne(genreId);
        if (genre == null) {
            return new ResponseEntity("Unable to upate. Genre with id " + genreId + " not found.", HttpStatus.NOT_FOUND);
        }
        genre.setName(genreObject.getName());
        genre.setDescription(genreObject.getDescription());
        genreRepository.save(genre);
        return new ResponseEntity(HttpStatus.OK);
    }



    /*@DELETE*/
    // Delete genre by id
    @DeleteMapping("/{genre_id}")
    public ResponseEntity deleteGenre(@PathVariable("genre_id") Long genreId)
    {
        Genre genre = genreRepository.findOne(genreId);
        if (genre == null) {
            return new ResponseEntity("Unable to delete.  Genre with id " + genreId + " not found.", HttpStatus.NOT_FOUND);
        }
        
        if (genre.getBooks().isEmpty()) {
            genreRepository.delete(genreId);
        } else {
            return new ResponseEntity("There are books associated to this genre", HttpStatus.CONFLICT);
        }
        
        return new ResponseEntity(HttpStatus.OK);
    }

}

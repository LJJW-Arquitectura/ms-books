package com.microservice.controller;

import com.microservice.response.AuthorResponse;
import com.microservice.entity.*;
import com.microservice.repository.*;
import com.microservice.response.BookResponse;
import com.microservice.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/books-ms/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;
    
    /*@POST*/
    // Post author
    @PostMapping("")
    public ResponseEntity createAuthor(@RequestBody Author author)
    {
        authorRepository.save(author);
        return new ResponseEntity(HttpStatus.CREATED);
    }    

    /*@GET*/
    // Get all authors
    @GetMapping("")
    public ResponseEntity findAllAuthors()
    {
        List<Author> authors = authorRepository.findAll();
        List<AuthorResponse> response = new ArrayList<>();
        for (Author author : authors) {
            response.add(new AuthorResponse(author));
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // Get author by ID
    @GetMapping("/{author_id}")
    public ResponseEntity findByAuthorId(@PathVariable("author_id") Long authorId)
    {
        Author author = authorRepository.findOne(authorId);
        if (author == null) {
            return new ResponseEntity(new ErrorResponse("Author with id " + authorId + " not found"), HttpStatus.NOT_FOUND);
        }
        AuthorResponse response = new AuthorResponse(author);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    // Get author by name
    @GetMapping("/name/{author_name}")
    public ResponseEntity findByAuthorName(@PathVariable("author_name") String authorName)
    {
        List<Author> authors = authorRepository.findByName(authorName);
        List<AuthorResponse> response = new ArrayList<>();
        for (Author author : authors) {
            response.add(new AuthorResponse(author));
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    // Get books of a author by ID
    @GetMapping("/{author_id}/books")
    @ResponseBody
    public ResponseEntity findBooksOfAuthor(@PathVariable("author_id") Long authorId) 
    {
        Author author = authorRepository.findOne(authorId);
        if (author == null) {
            return new ResponseEntity(new ErrorResponse("Author with id " + authorId + " not found"), HttpStatus.NOT_FOUND);
        }
        
        Set<Book> books = author.getBooks();
        List<BookResponse> response = new ArrayList<>();
        for (Book book : books) {
            response.add(new BookResponse(book));
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }



    /*@PUT*/
    // Put author by id
    @PutMapping("/{author_id}")
    public ResponseEntity updateAuthor(@PathVariable("author_id") Long authorId, @RequestBody Author authorObject)
    {
        Author author = authorRepository.findOne(authorId);
        if (author == null) {
            return new ResponseEntity(new ErrorResponse("Unable to upate. Author with id " + authorId + " not found."), HttpStatus.NOT_FOUND);
        }
        author.setName(authorObject.getName());
        author.setDescription(authorObject.getDescription());
        authorRepository.save(author);
        return new ResponseEntity(HttpStatus.OK);
    }



    /*@DELETE*/
    // Delete author by id
    @DeleteMapping("/{author_id}")
    public ResponseEntity deleteAuthor(@PathVariable("author_id") Long authorId)
    {
        Author author = authorRepository.findOne(authorId);
        if (author == null) {
            return new ResponseEntity(new ErrorResponse("Unable to delete.  Author with id " + authorId + " not found."), HttpStatus.NOT_FOUND);
        }
        
        if (author.getBooks().isEmpty()) {
            authorRepository.delete(authorId);
        } else {
            return new ResponseEntity(new ErrorResponse("There are books associated to this author"), HttpStatus.CONFLICT);
        }
        
        return new ResponseEntity(HttpStatus.OK);
    }

}

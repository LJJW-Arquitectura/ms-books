package com.microservice.controller;

import com.microservice.entity.*;
import com.microservice.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    /*@POST*/
    // Post author
    @PostMapping("/authors")
    public Author create(@RequestBody Author author)
    {
        return authorRepository.save(author);
    }



    /*@GET*/
    // Get all authors
    @GetMapping("/authors")
    public List<Author> findAll()
    {
        return authorRepository.findAll();
    }

    // Get author by ID
    @GetMapping("/authors/{author_id}")
    @ResponseBody
    public Author findByAuthorId(@PathVariable("author_id") Long authorId)
    {
        return authorRepository.findOne(authorId);
    }

    // Get author by name
    @GetMapping("/author/name/{author_name}")
    public List<Author> findByAuthorName(@PathVariable("author_name") String authorName)
    {
        return authorRepository.findByName(authorName);
    }

    // Get books of a author by ID
    @GetMapping("/authors/{author_id}/books")
    @ResponseBody
    public Set<Book> findBooksByAuthorId(@PathVariable("author_id") Long authorId) 
    {
        Author author = authorRepository.findOne(authorId);
        return author.getBooks();
    }



    /*@PUT*/
    // Put author by id
    @PutMapping("/authors/{author_id}")
    public Author update(@PathVariable("author_id") Long authorId, @RequestBody Author authorObject)
    {
        Author author = authorRepository.findOne(authorId);
        author.setName(authorObject.getName());
        author.setPhotoB64(authorObject.getPhotoB64());
        author.setDescription(authorObject.getDescription());
        return authorRepository.save(author);
    }



    /*@DELETE*/
    // Delete author by id
    @DeleteMapping("/authors/{author_id}")
    public List<Author> delete(@PathVariable("author_id") Long authorId)
    {
        authorRepository.delete(authorId);
        return authorRepository.findAll();
    }

}

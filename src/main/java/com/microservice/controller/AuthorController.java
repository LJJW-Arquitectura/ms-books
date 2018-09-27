package com.microservice.controller;

import com.microservice.entity.Author;
import com.microservice.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;


    @PostMapping("/authors")
    public Author create(@RequestBody Author author)
    {
        return authorRepository.save(author);
    }


    @GetMapping("/authors")
    public List<Author> findAll()
    {
        return authorRepository.findAll();
    }


    @PutMapping("/authors/{author_id}")
    public Author update(@PathVariable("author_id") Long authorId, @RequestBody Author authorObject)
    {
        Author author = authorRepository.findOne(authorId);
        author.setName(authorObject.getName());
        author.setPhoto_b64(authorObject.getPhoto_b64());
        author.setDescription(authorObject.getDescription());
        return authorRepository.save(author);
    }



    @DeleteMapping("/authors/{author_id}")
    public List<Author> delete(@PathVariable("author_id") Long authorId)
    {
        authorRepository.delete(authorId);
        return authorRepository.findAll();
    }



    @GetMapping("/authors/{author_id}")
    @ResponseBody
    public Author findByAuthorId(@PathVariable("author_id") Long authorId)
    {
        return authorRepository.findOne(authorId);
    }
}

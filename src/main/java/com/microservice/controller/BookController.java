package com.microservice.controller;

import com.microservice.entity.*;
import com.microservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // Post Book
    @PostMapping("/books")
    public Book create(@RequestBody Book book)
    {
        return bookRepository.save(book);
    }

    // Get all books
    @GetMapping("/books")
    public List<Book> findAll()
    {
        return bookRepository.findAll();
    }

    // Put book by id
    @PutMapping("/books/{book_id}")
    public Book update(@PathVariable("book_id") Long bookId, @RequestBody Book bookObject)
    {
        Book book = bookRepository.findOne(bookId);
        book.setTitle(bookObject.getTitle());
        book.setCover_b64(bookObject.getCover_b64());
        book.setPublisher(bookObject.getPublisher());
        // book.setAuthor(bookObject.getAuthor());
        book.setPages(bookObject.getPages());
        book.setIsbn(bookObject.getIsbn());
        book.setPlot(bookObject.getPlot());
        book.setGenre(bookObject.getGenre());
        return bookRepository.save(book);
    }


    // Delete book by id
    @DeleteMapping("/books/{book_id}")
    public List<Book> delete(@PathVariable("book_id") Long bookId)
    {
        bookRepository.delete(bookId);
        return bookRepository.findAll();
    }

    // Get book by ID
    @GetMapping("/books/{book_id}")
    @ResponseBody
    public Book findByBookId(@PathVariable("book_id") Long bookId)
    {
        return bookRepository.findOne(bookId);
    }

    // Get book by title
    @GetMapping("/books/title/{book_title}")
    public List<Book> findByBookTitle(@PathVariable("book_title") String bookTitle)
    {
        return bookRepository.findByTitle(bookTitle);
    }

}

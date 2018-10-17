package com.microservice.controller;

import com.microservice.entity.*;
import com.microservice.response.BookResponse;
import com.microservice.repository.*;
import com.microservice.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/books-ms/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    
    /*@POST*/
    // Post book
    @PostMapping("")
    public ResponseEntity createBook(@RequestBody Book book)
    {
        bookRepository.save(book);
        BookResponse response = new BookResponse(book);
        return new ResponseEntity(response, HttpStatus.CREATED);
    } 

    /*@GET*/
    // Get all books
    @GetMapping("")
    public ResponseEntity findAllBooks()
    {
        List<Book> books = bookRepository.findAll();
        List<BookResponse> response = new ArrayList<>();
        for (Book book : books) {
            response.add(new BookResponse(book));
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // Get book by ID
    @GetMapping("/{book_id}")
    public ResponseEntity findByBookId(@PathVariable("book_id") Long bookId)
    {
        if (!bookRepository.existsById(bookId)) {
            return new ResponseEntity(new ErrorResponse("Book with id " + bookId + " not found"), HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.getOne(bookId);
        BookResponse response = new BookResponse(book);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // Get book by title
    @GetMapping("/title/{book_title}")
    public ResponseEntity findByBookTitle(@PathVariable("book_title") String bookTitle)
    {
        List<Book> books = bookRepository.findByTitle(bookTitle);
        List<BookResponse> response = new ArrayList<>();
        for (Book book : books) {
            response.add(new BookResponse(book));
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /*@Put*/
    // Put book by id
    @PutMapping("/{book_id}")
    public ResponseEntity updateBook(@PathVariable("book_id") Long bookId, @RequestBody Book bookObject)
    {
        if (!bookRepository.existsById(bookId)) {
            return new ResponseEntity(new ErrorResponse("Unable to upate. Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.getOne(bookId);
        book.setTitle(bookObject.getTitle());
        book.setPublisher(bookObject.getPublisher());
        book.setNumPages(bookObject.getNumPages());
        book.setIsbn(bookObject.getIsbn());
        book.setPlot(bookObject.getPlot());
        book.setAuthors(bookObject.getAuthors());
        book.setGenres(bookObject.getGenres());
        bookRepository.save(book);
        BookResponse response = new BookResponse(book);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /*@DELETE*/
    // Delete book by id
    @DeleteMapping("/{book_id}")
    public ResponseEntity deleteBook(@PathVariable("book_id") Long bookId)
    {
        if (!bookRepository.existsById(bookId)) {
            return new ResponseEntity(new ErrorResponse("Unable to delete.  Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        } else {
            bookRepository.deleteById(bookId);
        }
        
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
}

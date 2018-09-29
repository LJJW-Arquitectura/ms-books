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

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;
    
    /*@POST*/
    // Book
    @PostMapping("/books")
    public Book create(@RequestBody Book book)
    {   
        return bookRepository.save(book);
    }

    @PostMapping("/books/{book_id}/authors/{author_id}")
    public Set<Author> AuthorsOfBook(@PathVariable("book_id") Long bookId, @PathVariable("author_id") Long authorId)
    {
        Author author = authorRepository.findOne(authorId);
        Book book = bookRepository.findOne(bookId);
  
        book.getAuthors().add(author);
        author.getBooks().add(book);

        bookRepository.save(book);
        authorRepository.save(author);
        
        return book.getAuthors();
    }

    @PostMapping("/books/{book_id}/genres/{genre_id}")
    public Set<Genre> GenresOfBook(@PathVariable("book_id") Long bookId, @PathVariable("genre_id") Long genreId)
    {
        Genre genre = genreRepository.findOne(genreId);
        Book book = bookRepository.findOne(bookId);
  
        book.getGenres().add(genre);
        genre.getBooks().add(book);

        bookRepository.save(book);
        genreRepository.save(genre);
        
        return book.getGenres();
    }



    /*@GET*/
    // Get all books
    @GetMapping("/books")
    public List<Book> findAll()
    {
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

    // Get authors of a book by ID
    @GetMapping("/books/{book_id}/authors")
    @ResponseBody
    public Set<Author> findAuthorsByBookId(@PathVariable("book_id") Long bookId) 
    {
        Book book = bookRepository.findOne(bookId);
        return book.getAuthors();
    }

    // Get genres of a book by ID
    @GetMapping("/books/{book_id}/genres")
    @ResponseBody
    public Set<Genre> findGenresByBookId(@PathVariable("book_id") Long bookId) 
    {
        Book book = bookRepository.findOne(bookId);
        return book.getGenres();
    }

    /*@Put*/
    // Put book by id
    @PutMapping("/books/{book_id}")
    public Book update(@PathVariable("book_id") Long bookId, @RequestBody Book bookObject)
    {
        Book book = bookRepository.findOne(bookId);
        book.setTitle(bookObject.getTitle());
        book.setCoverB64(bookObject.getCoverB64());
        book.setPublisher(bookObject.getPublisher());
        book.setNumPages(bookObject.getNumPages());
        book.setIsbn(bookObject.getIsbn());
        book.setPlot(bookObject.getPlot());
        return bookRepository.save(book);
    }

    /*@DELETE*/
    // Delete book by id
    @DeleteMapping("/books/{book_id}")
    public List<Book> delete(@PathVariable("book_id") Long bookId)
    {
        bookRepository.delete(bookId);
        return bookRepository.findAll();
    }

    // Delete author of a book
    @DeleteMapping("/books/{book_id}/authors/{author_id}")
    public Set<Author> DeleteAuthorsOfBook(@PathVariable("book_id") Long bookId, @PathVariable("author_id") Long authorId)
    {
        Author author = authorRepository.findOne(authorId);
        Book book = bookRepository.findOne(bookId);
  
        book.getAuthors().remove(author);
        author.getBooks().remove(book);

        bookRepository.save(book);
        authorRepository.save(author);
        
        return book.getAuthors();
    }

    // Delete genre of a book
    @DeleteMapping("/books/{book_id}/genres/{genre_id}")
    public Set<Genre> DeleteGenresOfBook(@PathVariable("book_id") Long bookId, @PathVariable("genre_id") Long genreId)
    {
        Genre genre = genreRepository.findOne(genreId);
        Book book = bookRepository.findOne(bookId);
  
        book.getGenres().remove(genre);
        genre.getBooks().remove(book);

        bookRepository.save(book);
        genreRepository.save(genre);
        
        return book.getGenres();
    }

}

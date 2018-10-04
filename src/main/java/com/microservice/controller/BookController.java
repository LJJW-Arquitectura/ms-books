package com.microservice.controller;

import com.microservice.entity.*;
import com.microservice.response.BookResponse;
import com.microservice.repository.*;
import com.microservice.response.AuthorResponse;
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

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;
    
    /*@POST*/
    // Post book
    @PostMapping("")
    public ResponseEntity createBook(@RequestBody Book book)
    {
        bookRepository.save(book);
        return new ResponseEntity(HttpStatus.CREATED);
    } 

    @PostMapping("/{book_id}/author/{author_id}")
    public ResponseEntity addAuthorToBook(@PathVariable("book_id") Long bookId, @PathVariable("author_id") Long authorId)
    {
        Author author = authorRepository.findOne(authorId);
        if (author == null) {
            return new ResponseEntity(new ErrorResponse("Author with id " + authorId + " not found"), HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Book with id " + authorId + " not found"), HttpStatus.NOT_FOUND);
        }
  
        book.getAuthors().add(author);
        author.getBooks().add(book);
        bookRepository.save(book);
        authorRepository.save(author);
        
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/{book_id}/genre/{genre_id}")
    public ResponseEntity addGenreToBook(@PathVariable("book_id") Long bookId, @PathVariable("genre_id") Long genreId)
    {
        Genre genre = genreRepository.findOne(genreId);
        if (genre == null) {
            return new ResponseEntity(new ErrorResponse("Genre with id " + genreId + " not found"), HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Book with id " + genreId + " not found"), HttpStatus.NOT_FOUND);
        }
  
        book.getGenres().add(genre);
        genre.getBooks().add(book);
        bookRepository.save(book);
        genreRepository.save(genre);
        
        return new ResponseEntity(HttpStatus.CREATED);
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
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Book with id " + bookId + " not found"), HttpStatus.NOT_FOUND);
        }
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
    
    // Get authors of a book by ID
    @GetMapping("/{book_id}/authors")
    @ResponseBody
    public ResponseEntity findAuthorsOfBook(@PathVariable("book_id") Long bookId) 
    {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Author with id " + bookId + " not found"), HttpStatus.NOT_FOUND);
        }
        
        Set<Author> authors = book.getAuthors();
        List<AuthorResponse> response = new ArrayList<>();
        for (Author author : authors) {
            response.add(new AuthorResponse(author));
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // Get genres of a book by ID
    @GetMapping("/{book_id}/genres")
    @ResponseBody
    public ResponseEntity findGenresOfBook(@PathVariable("book_id") Long bookId) 
    {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Genre with id " + bookId + " not found"), HttpStatus.NOT_FOUND);
        }
        
        Set<Genre> genres = book.getGenres();
        return new ResponseEntity(genres, HttpStatus.OK);
    }

    /*@Put*/
    // Put book by id
    @PutMapping("/{book_id}")
    public ResponseEntity updateBook(@PathVariable("book_id") Long bookId, @RequestBody Book bookObject)
    {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Unable to upate. Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        }
        book.setTitle(bookObject.getTitle());
        book.setPublisher(bookObject.getPublisher());
        book.setNumPages(bookObject.getNumPages());
        book.setIsbn(bookObject.getIsbn());
        book.setPlot(bookObject.getPlot());
        bookRepository.save(book);
        return new ResponseEntity(HttpStatus.OK);
    }

    /*@DELETE*/
    // Delete book by id
    @DeleteMapping("/{book_id}")
    public ResponseEntity deleteBook(@PathVariable("book_id") Long bookId)
    {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Unable to delete.  Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        } else {
            bookRepository.delete(bookId);
        }
        
        return new ResponseEntity(HttpStatus.OK);
    }


    // Delete author of a book
    @DeleteMapping("/{book_id}/author/{author_id}")
    public ResponseEntity removeAuthorOfBook(@PathVariable("book_id") Long bookId, @PathVariable("author_id") Long authorId)
    {
        Author author = authorRepository.findOne(authorId);
        if (author == null) {
            return new ResponseEntity(new ErrorResponse("Author with id " + authorId + " not found"), HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Book with id " + authorId + " not found"), HttpStatus.NOT_FOUND);
        }
  
        book.getAuthors().remove(author);
        author.getBooks().remove(book);
        bookRepository.save(book);
        authorRepository.save(author);
        
        return new ResponseEntity(HttpStatus.OK);
    }

    // Delete genre of a book
    @DeleteMapping("/{book_id}/genre/{genre_id}")
    public ResponseEntity removeGenreOfBook(@PathVariable("book_id") Long bookId, @PathVariable("genre_id") Long genreId)
    {
        Genre genre = genreRepository.findOne(genreId);
        if (genre == null) {
            return new ResponseEntity(new ErrorResponse("Genre with id " + genreId + " not found"), HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Book with id " + genreId + " not found"), HttpStatus.NOT_FOUND);
        }
  
        book.getGenres().remove(genre);
        genre.getBooks().remove(book);
        bookRepository.save(book);
        genreRepository.save(genre);
        
        return new ResponseEntity(HttpStatus.OK);
    }

}

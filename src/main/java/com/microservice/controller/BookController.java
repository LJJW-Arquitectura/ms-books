package com.microservice.controller;

import com.microservice.entity.*;
import com.microservice.response.BookResponse;
import com.microservice.repository.*;
import com.microservice.response.AuthorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/books")
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
    public ResponseEntity<?> create(@RequestBody Book book)
    {
        Book newBook = bookRepository.save(book);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    } 

    @PostMapping("/{book_id}/addAuthor/{author_id}")
    public ResponseEntity<?> AddAuthor(@PathVariable("book_id") Long bookId, @PathVariable("author_id") Long authorId)
    {
        Author author = authorRepository.findOne(authorId);
        if (author == null) {
            return new ResponseEntity("Author with id " + authorId + " not found", HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity("Book with id " + authorId + " not found", HttpStatus.NOT_FOUND);
        }
  
        book.getAuthors().add(author);
        author.getBooks().add(book);

        bookRepository.save(book);
        authorRepository.save(author);
        
        return new ResponseEntity<>(book.getAuthors(), HttpStatus.OK);
    }

    @PostMapping("/{book_id}/addGenre/{genre_id}")
    public ResponseEntity<?> AddGenre(@PathVariable("book_id") Long bookId, @PathVariable("genre_id") Long genreId)
    {
        Genre genre = genreRepository.findOne(genreId);
        if (genre == null) {
            return new ResponseEntity("Genre with id " + genreId + " not found", HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity("Book with id " + genreId + " not found", HttpStatus.NOT_FOUND);
        }
  
        book.getGenres().add(genre);
        genre.getBooks().add(book);

        bookRepository.save(book);
        genreRepository.save(genre);
        
        return new ResponseEntity<>(book.getGenres(), HttpStatus.OK);
    }


    /*@GET*/
    // Get all books
    @GetMapping("")
    public ResponseEntity<?> findAll()
    {
        List<Book> books = bookRepository.findAll();
        List<BookResponse> response = new ArrayList<>();
        for (Book book : books) {
            response.add(new BookResponse(book));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get book by ID
    @GetMapping("/{book_id}")
    public ResponseEntity<?> findByBookId(@PathVariable("book_id") Long bookId)
    {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity("Book with id " + bookId + " not found", HttpStatus.NOT_FOUND);
        }
        BookResponse response = new BookResponse(book);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get book by title
    @GetMapping("/title/{book_title}")
    public ResponseEntity<?> findByBookId(@PathVariable("book_title") String bookTitle)
    {
        List<Book> books = bookRepository.findByTitle(bookTitle);
        List<BookResponse> response = new ArrayList<>();
        for (Book book : books) {
            response.add(new BookResponse(book));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Get authors of a book by ID
    @GetMapping("/{book_id}/authors")
    @ResponseBody
    public ResponseEntity<?> findAuthors(@PathVariable("book_id") Long bookId) 
    {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity("Author with id " + bookId + " not found", HttpStatus.NOT_FOUND);
        }
        
        Set<Author> authors = book.getAuthors();
        List<AuthorResponse> response = new ArrayList<>();
        for (Author author : authors) {
            response.add(new AuthorResponse(author));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get genres of a book by ID
    @GetMapping("/{book_id}/genres")
    @ResponseBody
    public ResponseEntity<?> findGenres(@PathVariable("book_id") Long bookId) 
    {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity("Genre with id " + bookId + " not found", HttpStatus.NOT_FOUND);
        }
        
        Set<Genre> genres = book.getGenres();
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    /*@Put*/
    // Put book by id
    @PutMapping("/{book_id}")
    public ResponseEntity<?> update(@PathVariable("book_id") Long bookId, @RequestBody Book bookObject)
    {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity("Unable to upate. Book with id " + bookId + " not found.", HttpStatus.NOT_FOUND);
        }
        book.setTitle(bookObject.getTitle());
        book.setPublisher(bookObject.getPublisher());
        book.setNumPages(bookObject.getNumPages());
        book.setIsbn(bookObject.getIsbn());
        book.setPlot(bookObject.getPlot());
        bookRepository.save(book);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    /*@DELETE*/
    // Delete book by id
    @DeleteMapping("/{book_id}")
    public ResponseEntity<?> delete(@PathVariable("book_id") Long bookId)
    {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity("Unable to delete.  Book with id " + bookId + " not found.", HttpStatus.NOT_FOUND);
        } else {
            bookRepository.delete(bookId);
        }
        
        return new ResponseEntity<>("Successful delete", HttpStatus.OK);
    }


    // Delete author of a book
    @DeleteMapping("/{book_id}/authors/{author_id}")
    public ResponseEntity<?> DeleteAuthor(@PathVariable("book_id") Long bookId, @PathVariable("author_id") Long authorId)
    {
        Author author = authorRepository.findOne(authorId);
        if (author == null) {
            return new ResponseEntity("Author with id " + authorId + " not found", HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity("Book with id " + authorId + " not found", HttpStatus.NOT_FOUND);
        }
  
        book.getAuthors().remove(author);
        author.getBooks().remove(book);

        bookRepository.save(book);
        authorRepository.save(author);
        
        return new ResponseEntity<>(book.getAuthors(), HttpStatus.OK);
    }

    // Delete genre of a book
    @DeleteMapping("/{book_id}/genres/{genre_id}")
    public ResponseEntity<?> DeleteGenre(@PathVariable("book_id") Long bookId, @PathVariable("genre_id") Long genreId)
    {
        Genre genre = genreRepository.findOne(genreId);
        if (genre == null) {
            return new ResponseEntity("Genre with id " + genreId + " not found", HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            return new ResponseEntity("Book with id " + genreId + " not found", HttpStatus.NOT_FOUND);
        }
  
        book.getGenres().remove(genre);
        genre.getBooks().remove(book);

        bookRepository.save(book);
        genreRepository.save(genre);
        
        return new ResponseEntity<>(book.getGenres(), HttpStatus.OK);
    }

}

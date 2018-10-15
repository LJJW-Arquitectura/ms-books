package com.microservice.controller;

import com.microservice.entity.Author;
import com.microservice.entity.Book;
import com.microservice.entity.DBFile;
import com.microservice.response.UploadFileResponse;
import com.microservice.repository.AuthorRepository;
import com.microservice.repository.BookRepository;
import com.microservice.repository.DBFileRepository;
import com.microservice.response.ErrorResponse;
import com.microservice.service.DBFileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/books-ms/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private DBFileStorageService DBFileStorageService;
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private DBFileRepository dbFileRepository;
    
    /*POST*/
    // Post photo a auhtor
    @PostMapping("/author/{author_id}")
    public ResponseEntity uploadAuthorPhoto(@RequestParam("file") MultipartFile file, @PathVariable("author_id") Long authorId) {       
        Author author = authorRepository.getOne(authorId);
        if (author == null) {
            return new ResponseEntity(new ErrorResponse("Book with id " + authorId + " not found."), HttpStatus.NOT_FOUND);
        }
        if (author.getPhoto() != null) {
            return new ResponseEntity(new ErrorResponse("This author alredy has a photo"), HttpStatus.CONFLICT);
        }
        
        DBFile authorPhoto = DBFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/books-ms/file/downloadFile/")
                .path(authorPhoto.getId())
                .toUriString();

        author.setPhoto(authorPhoto);
        authorPhoto.setAuthor(author);
        authorRepository.save(author);
        dbFileRepository.save(authorPhoto);

        return new ResponseEntity(new UploadFileResponse(authorPhoto.getFileName(), fileDownloadUri, authorPhoto.getFileType(), authorPhoto.getFSize()), HttpStatus.CREATED);
    }
    
    // Post cover a book
    @PostMapping("/book/{book_id}")
    public ResponseEntity uploadBookCover(@RequestParam("file") MultipartFile file, @PathVariable("book_id") Long bookId) {
        Book book = bookRepository.getOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        }
        if (book.getCover() != null) {
            return new ResponseEntity(new ErrorResponse("This book alredy hass a cover"), HttpStatus.CONFLICT);
        }
        
        DBFile bookCover = DBFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/books-ms/file/downloadFile/")
                .path(bookCover.getId())
                .toUriString();
        
        book.setCover(bookCover);
        bookCover.setBook(book);
        bookRepository.save(book);
        dbFileRepository.save(bookCover);

        return new ResponseEntity(new UploadFileResponse(bookCover.getFileName(), fileDownloadUri, bookCover.getFileType(), bookCover.getFSize()), HttpStatus.CREATED);
    }

    /*GET*/
    // Get file
    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        DBFile authorPhoto = DBFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(authorPhoto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + authorPhoto.getFileName() + "\"")
                .body(new ByteArrayResource(authorPhoto.getData()));
    }
    
    /*PUT*/
    // Put cover a book
    @PutMapping("/book/{book_id}")
    public ResponseEntity updateBookCover(@RequestParam("file") MultipartFile file, @PathVariable("book_id") Long bookId) {
        Book book = bookRepository.getOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        }
        
        DBFile oldCover = dbFileRepository.getOne(book.getCover().getId());
        if (oldCover != null) {
            book.setCover(null);
            oldCover.setBook(null);
            bookRepository.save(book);
            dbFileRepository.save(oldCover);
            dbFileRepository.delete(oldCover);
        }
        
        DBFile bookCover = DBFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/books-ms/file/downloadFile/")
                .path(bookCover.getId())
                .toUriString();
        
        book.setCover(bookCover);
        bookCover.setBook(book);
        bookRepository.save(book);
        dbFileRepository.save(bookCover);

        return new ResponseEntity(new UploadFileResponse(bookCover.getFileName(), fileDownloadUri, bookCover.getFileType(), bookCover.getFSize()), HttpStatus.OK);
    }

    // Put photo a author
    @PutMapping("/author/{author_id}")
    public ResponseEntity updateAuthorPhoto(@RequestParam("file") MultipartFile file, @PathVariable("author_id") Long authorId) {
        Author author = authorRepository.getOne(authorId);
        if (author == null) {
            return new ResponseEntity(new ErrorResponse("Author with id " + authorId + " not found."), HttpStatus.NOT_FOUND);
        }
        
        DBFile oldPhoto = dbFileRepository.getOne(author.getPhoto().getId());
        if (oldPhoto != null) {
            // Delete from repository
            author.setPhoto(null);
            oldPhoto.setAuthor(null);
            authorRepository.save(author);
            dbFileRepository.save(oldPhoto);
            dbFileRepository.delete(oldPhoto);
        }
        
        DBFile authorPhoto = DBFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/books-ms/file/downloadFile/")
                .path(authorPhoto.getId())
                .toUriString();
        
        author.setPhoto(authorPhoto);
        authorPhoto.setAuthor(author);
        authorRepository.save(author);
        dbFileRepository.save(authorPhoto);

        return new ResponseEntity(new UploadFileResponse(authorPhoto.getFileName(), fileDownloadUri, authorPhoto.getFileType(), authorPhoto.getFSize()), HttpStatus.OK);
    }

    
    /*DELETE*/
    // Delete cover a book
    @DeleteMapping("/book/{book_id}")
    public ResponseEntity deleteBookCover(@PathVariable("book_id") Long bookId) {
        Book book = bookRepository.getOne(bookId);
        if (book == null) {
            return new ResponseEntity(new ErrorResponse("Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        }
        
        DBFile cover = dbFileRepository.getOne(book.getCover().getId());
        if (cover== null) {
            return new ResponseEntity(new ErrorResponse("This book dont have cover"), HttpStatus.CONFLICT);
        }
        
        // Delete from repository
        book.setCover(null);
        cover.setBook(null);
        bookRepository.save(book);
        dbFileRepository.save(cover);
        dbFileRepository.delete(cover);
        
        return new ResponseEntity(HttpStatus.OK);
    }
    
    // Delete photo a author
    @DeleteMapping("/author/{author_id}")
    public ResponseEntity deleteAuthorPhoto(@PathVariable("author_id") Long authorId) {
        Author author = authorRepository.getOne(authorId);
        if (author == null) {
            return new ResponseEntity(new ErrorResponse("Author with id " + authorId + " not found."), HttpStatus.NOT_FOUND);
        }
        
        DBFile photo = dbFileRepository.getOne(author.getPhoto().getId());
        if (photo== null) {
            return new ResponseEntity(new ErrorResponse("This author dont have photo"), HttpStatus.CONFLICT);
        }
        
        // Delete from repository
        author.setPhoto(null);
        photo.setAuthor(null);
        authorRepository.save(author);
        dbFileRepository.save(photo);
        dbFileRepository.delete(photo);
        
        return new ResponseEntity(HttpStatus.OK);
    }
    
}

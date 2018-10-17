package com.microservice.controller;

import com.microservice.entity.Book;
import com.microservice.entity.DBFile;
import com.microservice.response.UploadFileResponse;
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
    private BookRepository bookRepository;
    
    @Autowired
    private DBFileRepository dbFileRepository;
    
    /*POST*/
    // Post cover a book
    @PostMapping("/book/{book_id}")
    public ResponseEntity uploadBookCover(@RequestParam("file") MultipartFile file, @PathVariable("book_id") Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            return new ResponseEntity(new ErrorResponse("Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.getOne(bookId);
        if (book.getCover() != null) {
            return new ResponseEntity(new ErrorResponse("This book alredy has a cover"), HttpStatus.CONFLICT);
        }
        
        DBFile bookCover = DBFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/books-ms/file/download/")
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
    @GetMapping("/download/{file_id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("file_id") String fileId) {
        // Load file from database
        
        DBFile file = DBFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getData()));
    }
    
    /*PUT*/
    // Put cover a book
    @PutMapping("/book/{book_id}")
    public ResponseEntity updateBookCover(@RequestParam("file") MultipartFile file, @PathVariable("book_id") Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            return new ResponseEntity(new ErrorResponse("Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        }
        
        Book book = bookRepository.getOne(bookId);
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
                .path("/books-ms/file/download/")
                .path(bookCover.getId())
                .toUriString();
        
        book.setCover(bookCover);
        bookCover.setBook(book);
        bookRepository.save(book);
        dbFileRepository.save(bookCover);

        return new ResponseEntity(new UploadFileResponse(bookCover.getFileName(), fileDownloadUri, bookCover.getFileType(), bookCover.getFSize()), HttpStatus.OK);
    }
    
    /*DELETE*/
    // Delete cover a book
    @DeleteMapping("/book/{book_id}")
    public ResponseEntity deleteBookCover(@PathVariable("book_id") Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            return new ResponseEntity(new ErrorResponse("Book with id " + bookId + " not found."), HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.getOne(bookId);
        
        if (book.getCover() == null) {
            return new ResponseEntity(new ErrorResponse("This book don't has a cover"), HttpStatus.CONFLICT);
        }
        DBFile cover = dbFileRepository.getOne(book.getCover().getId());
        
        // Delete from repository
        book.setCover(null);
        cover.setBook(null);
        bookRepository.save(book);
        dbFileRepository.save(cover);
        dbFileRepository.delete(cover);
        
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
       
}

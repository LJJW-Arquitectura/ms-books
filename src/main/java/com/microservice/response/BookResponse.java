package com.microservice.response;

import com.microservice.entity.Book;
import com.microservice.entity.DBFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


public class BookResponse {
    private Long id;
    
    private String title;
    private String publisher;
    private int numPages;
    private String isbn;
    private String plot;
    private String[] authors;
    private String[] genres;
    private UploadFileResponse cover;

    public BookResponse(Book book) {
        DBFile dbCover = book.getCover();
        UploadFileResponse coverResponse = null;
        if (dbCover != null) {
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/books-ms/file/download/").path(dbCover.getId()).toUriString();
            coverResponse = new UploadFileResponse(dbCover.getFileName(), fileDownloadUri, dbCover.getFileType(), dbCover.getFSize());
        }
        
        this.id = book.getId();
        this.title = book.getTitle();
        this.publisher = book.getPublisher();
        this.numPages = book.getNumPages();
        this.isbn = book.getIsbn();
        this.plot = book.getPlot();
        this.authors = book.getAuthors();
        this.genres = book.getGenres();
        this.cover = coverResponse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public UploadFileResponse getCover() {
        return cover;
    }

    public void setCover(UploadFileResponse cover) {
        this.cover = cover;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }
     
}

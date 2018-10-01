package com.microservice.response;

import com.microservice.entity.Author;
import com.microservice.entity.DBFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


public class AuthorResponse {
    private long id;
    private String name;
    private String description;
    private UploadFileResponse photo;

    public AuthorResponse(Author author) {
        DBFile dbPhoto = author.getPhoto();
        UploadFileResponse photoResponse = null;
        if (dbPhoto != null) {
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/books-ms/file/downloadFile/").path(dbPhoto.getId()).toUriString();
            photoResponse = new UploadFileResponse(dbPhoto.getFileName(), fileDownloadUri, dbPhoto.getFileType(), dbPhoto.getFSize());
        }
    
        this.id = author.getId();
        this.name = author.getName();
        this.description = author.getDescription();
        this.photo = photoResponse;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UploadFileResponse getPhoto() {
        return photo;
    }

    public void setPhoto(UploadFileResponse photo) {
        this.photo = photo;
    }
    
    
}

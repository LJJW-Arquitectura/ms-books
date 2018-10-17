package com.microservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@Entity
@Table(name = "file")
public class DBFile implements Serializable{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;
    private String fileType;
    private long fSize;

    @Lob
    @JsonIgnore
    private byte[] data;
    
    @OneToOne(fetch = FetchType.LAZY,
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        },
            mappedBy = "cover")
    @JsonIgnore
    private Book book;

    // Constructors
    public DBFile() {

    }

    public DBFile(String fileName, String fileType, byte[] data, long fSize) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.fSize = fSize;
    }
    
    // Getter and Setter
    public long getfSize() {
        return fSize;
    }

    public void setfSize(long fSize) {
        this.fSize = fSize;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getFSize() {
        return fSize;
    }

    public void setFSize(long fSize) {
        this.fSize = fSize;
    }
    
}

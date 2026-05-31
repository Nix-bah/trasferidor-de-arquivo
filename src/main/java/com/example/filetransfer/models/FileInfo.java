package com.example.filetransfer.models;

import java.time.LocalDateTime;

public class FileInfo {

    private String id;
    private String originalName;
    private String storedName;
    private long size;
    private LocalDateTime uploadDate;
    private LocalDateTime expirationDate;
    private long downloads;

    public FileInfo() {
    }

    public FileInfo(
            String id,
            String originalName,
            String storedName,
            long size,
            LocalDateTime uploadDate,
            LocalDateTime expirationDate
    ) {
        this.id = id;
        this.originalName = originalName;
        this.storedName = storedName;
        this.size = size;
        this.uploadDate = uploadDate;
        this.expirationDate = expirationDate;
        this.downloads = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }
}
package com.example.filetransfer.models;

public class UploadResponse {

    private String id;
    private String fileName;
    private String shareLink;

    public UploadResponse() {
    }

    public UploadResponse(String id,
                          String fileName,
                          String shareLink) {

        this.id = id;
        this.fileName = fileName;
        this.shareLink = shareLink;
    }

    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }
}
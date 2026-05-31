package com.example.filetransfer.controllers;

import com.example.filetransfer.models.FileInfo;
import com.example.filetransfer.models.UploadResponse;
import com.example.filetransfer.services.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileStorageService service;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public UploadResponse upload(
            @RequestParam("file")
            MultipartFile file,
            HttpServletRequest request
    ) {

        FileInfo info =
                service.store(file);

        String link =
                request.getScheme()
                        + "://"
                        + request.getServerName()
                        + ":"
                        + request.getServerPort()
                        + "/file/"
                        + info.getId();

        return new UploadResponse(
                info.getId(),
                info.getOriginalName(),
                link
        );
    }

    @GetMapping
    public List<FileInfo> list() {
        return service.list();
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable String id
    ) {
        service.delete(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(
            @PathVariable String id
    ) throws Exception {

        Path path =
                service.getPath(id);

        Resource resource =
                new UrlResource(
                        path.toUri()
                );

        FileInfo info =
                service.get(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + info.getOriginalName()
                                + "\""
                )
                .body(resource);
    }

}
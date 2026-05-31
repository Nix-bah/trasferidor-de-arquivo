package com.example.filetransfer.services;

import com.example.filetransfer.exceptions.FileStorageException;
import com.example.filetransfer.models.FileInfo;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileStorageService {

    private final Map<String, FileInfo> files = new ConcurrentHashMap<>();

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.expiration-hours}")
    private long expirationHours;

    private final Tika tika = new Tika();

    @PostConstruct
    public void init() {

        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public FileInfo store(MultipartFile file) {

        try {

            if (file.isEmpty()) {
                throw new FileStorageException("Arquivo vazio.");
            }

            String mime = tika.detect(file.getInputStream());

            if (mime.contains("x-msdownload")) {
                throw new FileStorageException(
                        "Tipo de arquivo bloqueado."
                );
            }

            String uuid = UUID.randomUUID().toString();

            String originalName =
                    Objects.requireNonNull(file.getOriginalFilename());

            String cleanName =
                    originalName.replaceAll("[^a-zA-Z0-9._-]", "_");

            String storedName =
                    uuid + "_" + cleanName;

            Path target =
                    Paths.get(uploadDir)
                            .resolve(storedName);

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            FileInfo info = new FileInfo(
                    uuid,
                    originalName,
                    storedName,
                    file.getSize(),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(expirationHours)
            );

            files.put(uuid, info);

            return info;

        } catch (Exception e) {
            throw new FileStorageException(
                    "Falha ao salvar arquivo.",
                    e
            );
        }
    }

    public List<FileInfo> list() {

        removeExpired();

        return new ArrayList<>(files.values());
    }

    public FileInfo get(String id) {

        removeExpired();

        FileInfo info = files.get(id);

        if (info == null) {
            throw new FileStorageException("Arquivo não encontrado.");
        }

        return info;
    }

    public Path getPath(String id) {

        FileInfo info = get(id);

        info.setDownloads(
                info.getDownloads() + 1
        );

        return Paths.get(uploadDir)
                .resolve(info.getStoredName());
    }

    public void delete(String id) {

        FileInfo info = get(id);

        try {

            Files.deleteIfExists(
                    Paths.get(uploadDir)
                            .resolve(info.getStoredName())
            );

            files.remove(id);

        } catch (IOException e) {

            throw new FileStorageException(
                    "Falha ao excluir arquivo."
            );
        }
    }

    private void removeExpired() {

        LocalDateTime now =
                LocalDateTime.now();

        List<String> expired =
                files.values()
                        .stream()
                        .filter(f ->
                                f.getExpirationDate()
                                        .isBefore(now))
                        .map(FileInfo::getId)
                        .toList();

        expired.forEach(id -> {

            try {

                Files.deleteIfExists(
                        Paths.get(uploadDir)
                                .resolve(
                                        files.get(id)
                                                .getStoredName()
                                )
                );

            } catch (Exception ignored) {
            }

            files.remove(id);

        });

    }
}
package com.example.filetransfer.controllers;

import com.example.filetransfer.models.FileInfo;
import com.example.filetransfer.services.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ViewController {

    @Autowired
    private FileStorageService service;

    @GetMapping("/file/{id}")
    public String filePage(
            @PathVariable String id,
            Model model
    ) {

        FileInfo info =
                service.get(id);

        model.addAttribute(
                "file",
                info
        );

        return "download";
    }

}
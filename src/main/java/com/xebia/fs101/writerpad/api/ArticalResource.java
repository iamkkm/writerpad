package com.xebia.fs101.writerpad.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class ArticalResource {

    @PostMapping
    public ResponseEntity<Void> create(){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

package com.xebia.fs101.writerpad.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(MailException.class)
    void mailException(Exception ex) {
    }

    @ExceptionHandler(WriterpadException.class)
    ResponseEntity<?> mailException(WriterpadException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getContext());
    }
}

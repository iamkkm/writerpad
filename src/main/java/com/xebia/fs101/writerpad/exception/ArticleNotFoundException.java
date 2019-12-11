package com.xebia.fs101.writerpad.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Article is not present.")
public class ArticleNotFoundException extends RuntimeException {
}
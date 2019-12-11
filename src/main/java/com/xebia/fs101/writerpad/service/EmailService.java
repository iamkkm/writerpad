package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;

import java.util.concurrent.ExecutionException;


public interface EmailService {

    void sendMail(Article article) throws ExecutionException, InterruptedException;
}

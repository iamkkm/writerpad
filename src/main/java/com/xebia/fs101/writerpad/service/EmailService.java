package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;


public interface EmailService {

    void sendMail(Article article);
}

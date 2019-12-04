package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class NoopEmailService implements EmailService {

    @Override
    public void sendMail(Article article) {

    }
}

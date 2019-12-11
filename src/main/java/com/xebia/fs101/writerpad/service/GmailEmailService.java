package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Profile("!test")
@Service
public class GmailEmailService implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${email.to}")
    private String emailTo;
    @Value("${email.subject}")
    private String subject;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    //@Async
    @Override
    public void sendMail(Article article) throws ExecutionException, InterruptedException {

        Runnable emailtask = () -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailTo);
            message.setSubject(subject);
            message.setText("Your article titled " + article.getTitle() + " pulished on web");
            javaMailSender.send(message);
        };

        Future<?> submit = this.executorService.submit(emailtask);
    }
}

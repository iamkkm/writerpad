package com.xebia.fs101.writerpad.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class SpamCheckerTest {

    @Autowired
    private SpamChecker spamChecker;

    @Test
    void should_check_if_string_contains_sapm() throws IOException {
        String input = "Hello";
        boolean spam = spamChecker.isSpam(input);
        assertThat(spam).isFalse();
    }
    @Test
    void should_return_true_if_spam() throws IOException {
        String input = "adult";
        boolean spam = spamChecker.isSpam(input);
        assertThat(spam).isTrue();
    }
}
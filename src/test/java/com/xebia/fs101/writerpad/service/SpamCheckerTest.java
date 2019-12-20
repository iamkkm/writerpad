package com.xebia.fs101.writerpad.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class SpamCheckerTest {

    private SpamChecker spamChecker = new SpamChecker();

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
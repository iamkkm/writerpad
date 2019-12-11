package com.xebia.fs101.writerpad.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class ReadTimeCalculatorTest {

    private ReadTimeCalculator readTimeCalculator;

    @BeforeEach
    void setUp() {
        readTimeCalculator = new ReadTimeCalculator();
        readTimeCalculator.speed = 2;
    }

    @Test
    void should_return_read_time_of_article() {
        String content = "This is plain text for testing purpose";
        Duration duration=readTimeCalculator.calculateReadingTime(content);
        assertThat(duration.toMinutes()).isEqualTo(0);
        assertThat(duration.getSeconds()).isEqualTo(3);

    }
}

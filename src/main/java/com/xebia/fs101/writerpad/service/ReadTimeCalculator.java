package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class ReadTimeCalculator {

    @Value("${spring.service.speed.per.second}")
    double speed;
    private int numOfWords;
    private int min;
    private int sec;

    public Duration calculateReadingTime(String content) {
        int totalWords = StringUtil.getTotalWords(content);
        int readingTimeInSeconds = (int) (totalWords / speed);
        return Duration.of(readingTimeInSeconds, ChronoUnit.SECONDS);
    }
}

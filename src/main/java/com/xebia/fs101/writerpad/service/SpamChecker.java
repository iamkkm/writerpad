package com.xebia.fs101.writerpad.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class SpamChecker {

    @Value("${spam.file}")
    private File file;

    private List<String> lines;

    public boolean isSpam(String content) throws IOException {
        lines = Files.readAllLines(file.toPath());
        String[] words = content.toLowerCase().split("\\s");
        for (String word : words) {
            if (lines.contains(word)) {
                return true;
            }
        }
        return false;
    }
}

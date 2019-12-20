package com.xebia.fs101.writerpad.service;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SpamChecker {

    Set<String> spamWords;
    List<String> lines = new ArrayList<>();

    public boolean isSpam(String content) throws IOException {
        File file = ResourceUtils.getFile("classpath:spam.txt");
        FileInputStream input = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        br.close();
        spamWords = new HashSet<>(lines);
        Set<String> words = new HashSet<>(
                Arrays.asList(content.toLowerCase().split("\\s")));
        if (!Collections.disjoint(spamWords, words))
            return true;
        return false;
    }

    /*@Value("${spam.file}")
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
    }*/
}

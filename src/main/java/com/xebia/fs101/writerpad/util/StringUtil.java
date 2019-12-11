package com.xebia.fs101.writerpad.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class StringUtil {
    public static String slug(String title) {
        return title.trim()
                .replaceAll(" ", "-")
                .toLowerCase();
    }

    public static List<String> convertToLowercase(String[] tags) {
        return Arrays.stream(tags)
                .map(String::toLowerCase)
                .map(StringUtil::slug)
                .collect(Collectors.toList());
    }
    public static UUID toUuid(String input) {
        return UUID.fromString(input.substring(input.length() - 36));
    }

    public static int getTotalWords(String content) {
        return content.split("\\s|\\.").length;
    }
}

package com.xebia.fs101.writerpad.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.xebia.fs101.writerpad.util.StringUtil.convertToLowercase;
import static com.xebia.fs101.writerpad.util.StringUtil.slug;
import static org.assertj.core.api.Assertions.assertThat;

class StringUtilTest {

    @Test
    void should_generate_slug(){
        String title = "Hello! My name is Kamal";
        String expected = slug(title);
        assertThat(expected).isEqualTo("hello!-my-name-is-kamal");
    }

    @Test
    void should_convert_to_lowercase(){
        String[] tags = {"Hello", "World"};
        List<String> tagsList = convertToLowercase(tags);
        assertThat(tagsList).contains("hello", "world");
    }

}
package com.xebia.fs101.writerpad.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticalResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void mock_mvn_not_null() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void check_api_articles_post_request_status_as_created() throws Exception {
        String mockJson = "{\n" +
                "  \"title\": \"How to learn Spring Booot\",\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"body\": \"You have to believe\",\n" +
                "  \"tags\": [\"java\", \"Spring Boot\", \"tutorial\"],\n" +
                "  \"featuredImage\": \"url of the featured image\"\n" +
                "}";
        mockMvc.perform( post("/api/articles")
                .content(mockJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void should_give_status_bad_request() throws Exception {
        String json = "{\n" +
                "  \"title\": \"\",\n" +
                "  \"description\": \"Ever wonder how?\",\n" +
                "  \"tags\": [\"java\", \"Spring Boot\", \"tutorial\"],\n" +
                "  \"featuredImage\": \"url of the featured image\"\n" +
                "}";
        mockMvc.perform(post("/api/articles")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
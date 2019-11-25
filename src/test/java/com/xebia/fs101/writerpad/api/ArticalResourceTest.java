package com.xebia.fs101.writerpad.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class ArticalResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void mock_mvn_not_null(){
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void should_get_response_printed() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/articles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}
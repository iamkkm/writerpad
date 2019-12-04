package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.api.representations.CommentRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.CommentRepository;
import com.xebia.fs101.writerpad.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentResourceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void should_add_a_valid_comment_to_article() throws Exception {
        Article article = createArticle();
        Article savedArticle = this.articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        CommentRequest commentRequest = new CommentRequest("comment");
        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/api/articles/{slugUuid}/comments", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body")
                        .value("comment"))
                .andExpect(jsonPath("$.ipAddress")
                        .isNotEmpty());
    }

    @Test
    void should_not_add_a_comment_with_spam_words() throws Exception {
        Article article = createArticle();
        Article savedArticle = this.articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());
        CommentRequest commentRequest = new CommentRequest("adult");
        String json = objectMapper.writeValueAsString(commentRequest);
        mockMvc.perform(post("/api/articles/{slugUuid}/comments", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_all_comments() throws Exception {
        Article article = createArticle();
        Article savedArticle = this.articleRepository.save(article);
        String id = String.format("%s-%s", savedArticle.getSlug(), savedArticle.getId());

        commentRepository.save(new Comment.Builder().withArticle(article).withBody("comment1").build());
        mockMvc.perform(get("/api/articles/{slugUuid}/comments", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));;
    }

    @Test
    void should_be_able_to_return_404_status_for_delete_operation_when_article_is_not_present() throws Exception {
        Article article = createArticle();
        Article saved = articleRepository.save(article);
        String id = "great-world-" + saved.getId() + "abc";
        mockMvc.perform(delete("/api/articles/{slugUuid}/comments/{id}", id, 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void should_be_able_to_return_204_status_for_delete_operation() throws Exception {
        Article article = createArticle();
        Article saved = articleRepository.save(article);
        Comment comment1 = new Comment.Builder()
                .withArticle(saved)
                .withBody("yeah1")
                .build();
        Comment comment2 = new Comment.Builder()
                .withArticle(saved)
                .withBody("yeah2")
                .build();
        commentRepository.saveAll(Arrays.asList(comment1, comment2));
        String id = String.format("%s-%s", saved.getSlug(), saved.getId());
        mockMvc.perform(delete("/api/articles/{slugUuid}/comments/{id}", id, comment1.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private Article createArticle() {
        return new Article.Builder()
                .withTitle("Title")
                .withDescription("description")
                .withBody("body")
                .build();
    }
}

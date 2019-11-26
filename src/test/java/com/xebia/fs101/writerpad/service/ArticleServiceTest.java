package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.api.ArticleResource;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
@AutoConfigureMockMvc
@SpringBootTest
public class ArticleServiceTest {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ArticleResource articleResource;
    @Autowired
    MockMvc mockMvc;
    @Test
    public void should_be_able_to_save_valid_article() throws Exception {
        assertThat(articleRepository);
        ArticleRequest articleRequest = new ArticleRequest.Builder().
                withTitle("junit").
                withDescription("used for testing").
                withBody("this is body for test case").
                withTags(new String[]{"abc","def"}).
                build();
        long countBeforeAdd = articleRepository.count();
        ResponseEntity<Article> articleResponseEntity = articleResource.saveArticle(articleRequest);
        long countAfterAdd = articleRepository.count();
        assertThat(countAfterAdd - countBeforeAdd).isEqualTo(1);
        assertThat(articleResponseEntity.getStatusCode()== HttpStatus.CREATED);
    }
}

package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private ArticleService articleService;

    @Test
    void should_return_false_when_article_is_already_in_publish_state() {
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .build();
        article.setStatus(ArticleStatus.PUBLISHED);
        Article published = articleService.publish(article);
        assertThat(published).isNull();
    }

    @Test
    void should_return_true_when_article_is_published() {
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .build();
        Article published = articleService.publish(article);
        assertThat(published).isNull();
        verify(articleRepository).save(article);
        Mockito.verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_save_an_article() {
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .build();
        when(articleRepository.save(article)).thenReturn(article);
        Article saved = articleService.save(article);
        assertThat(saved.getTitle()).isEqualTo("title");
        assertThat(saved.getDescription()).isEqualTo("description");
        assertThat(saved.getBody()).isEqualTo("body");
        verify(articleRepository).save(article);
        Mockito.verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_return_empty_optional_when_article_does_not_exist() {
        String id = UUID.randomUUID().toString();
        when(articleRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());
        Optional<Article> empty = articleService.update(id, new Article());
        assertThat(empty).isEmpty();
    }

    @Test
    void should_return_article_when_article_exist() {
        String id = UUID.randomUUID().toString();
        Article articleInDb = new Article.Builder()
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .build();
        when(articleRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(articleInDb));
        when(articleRepository.save(articleInDb)).thenReturn(articleInDb);
        Optional<Article> updated = articleService.update(id, new Article.Builder().withTitle("updated title").build());
        assertThat(updated).isNotEmpty();
        assertThat(updated.get().getTitle()).isEqualTo("updated title");
        assertThat(updated.get().getDescription()).isEqualTo("description");
        assertThat(updated.get().getBody()).isEqualTo("body");
        verify(articleRepository).save(any());
        Mockito.verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_return_true_if_existing_article_is_deleted() {
        String id = UUID.randomUUID().toString();
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .build();
        when(articleRepository.findById(UUID.fromString(id))).thenReturn(Optional.of(article));
        boolean deleted = articleService.delete(id);
        assertThat(deleted).isTrue();
        verify(articleRepository).deleteById(article.getId());
        Mockito.verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_return_false_if_article_to_be_deleted_does_not_exist() {
        String id = UUID.randomUUID().toString();
        when(articleRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());
        boolean deleted = articleService.delete(id);
        assertThat(deleted).isFalse();
        Mockito.verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_find_article_by_status() {
        Pageable pageable = PageRequest.of(0, 10);
        articleService.findByStatus("DRAFT", pageable);
        verify(articleRepository).findAllByStatus(ArticleStatus.valueOf("DRAFT"), pageable);
        Mockito.verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_find_all_articles() {
        Pageable pageable = PageRequest.of(0, 10);
        articleService.findAllArticles(pageable);
        verify(articleRepository).findAll(pageable);
        Mockito.verifyNoMoreInteractions(articleRepository);
    }
}
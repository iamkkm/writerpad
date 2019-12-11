package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
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
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_save_an_article() {
        Article article = new Article.Builder()
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .build();
        User user = new User.Builder()
                .withUsername("kamal")
                .withEmail("kamal@mail.com")
                .withPassword("abc")
                .build();
        article.setUser(user);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        articleService.save(article, user);
        verify(articleRepository).save(article);
        verifyNoMoreInteractions(articleRepository);
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
        verifyNoMoreInteractions(articleRepository);
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
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_return_false_if_article_to_be_deleted_does_not_exist() {
        String id = UUID.randomUUID().toString();
        when(articleRepository.findById(UUID.fromString(id))).thenReturn(Optional.empty());
        boolean deleted = articleService.delete(id);
        assertThat(deleted).isFalse();
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_find_article_by_status() {
        Pageable pageable = PageRequest.of(0, 10);
        articleService.findByStatus("DRAFT", pageable);
        verify(articleRepository).findAllByStatus(ArticleStatus.valueOf("DRAFT"), pageable);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_find_all_articles() {
        Pageable pageable = PageRequest.of(0, 10);
        articleService.findAllArticles(pageable);
        verify(articleRepository).findAll(pageable);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_publish_article() {
        Article article = new Article.Builder()
                .withId(UUID.randomUUID())
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .build();
        when(articleRepository.save(article)).thenReturn(article);
        articleService.publish(article);
        verify(articleRepository).save(article);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    void should_be_able_to_find_tags() {
        Article article = new Article.Builder()
                .withId(UUID.randomUUID())
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .withTags(Arrays.asList("Java", "Spring Boot", "tutorial"))
                .build();
        when(articleRepository.findAllTags()).thenReturn(Stream.of("Java", "Spring Boot", "tutorial"));
        articleService.findTagsWithOccurence();
        verify(articleRepository).findAllTags();
        verifyNoMoreInteractions(articleRepository);

    }

    @Test
    void should_be_able_to_mark_article_as_favorite() {
        Article article = new Article.Builder()
                .withId(UUID.randomUUID())
                .withBody("body")
                .withDescription("description")
                .withTitle("title")
                .withTags(Arrays.asList("Java", "Spring Boot", "tutorial"))
                .build();
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        articleService.favoritedArticle(article.getSlug() + "-" + article.getId());
        verify(articleRepository).save(article);
        verifyNoMoreInteractions(articleRepository);


    }

    @Test
    void should_be_able_to_mark_article_as_unfavorite() {
        Article article = new Article.Builder()
                .withId(UUID.randomUUID())
                .withBody("body")
                .withDescription("description")
                .withFavorited(true)
                .withFavoriteCount(5)
                .withTitle("title")
                .withTags(Arrays.asList("Java", "Spring Boot", "tutorial"))
                .build();
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
        articleService.unfavoritedArticle(article.getSlug() + "-" + article.getId());
        verify(articleRepository).save(article);
        verifyNoMoreInteractions(articleRepository);


    }
}
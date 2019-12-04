package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static com.xebia.fs101.writerpad.util.StringUtil.toUuid;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Article save(Article article) {
        return articleRepository.save(article);

    }

    public Optional<Article> findArticle(String slugUuid) {
        UUID id = UUID.fromString(slugUuid);
        Optional<Article> optionalArticle = articleRepository.findById(id);
        return optionalArticle;
    }

    public Optional<Article> findArticleById(String slugUuid) {
        return articleRepository.findById(toUuid(slugUuid));
    }

    public Optional<Article> update(String slugUuid, Article copyFrom) {
        UUID id = toUuid(slugUuid);
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return Optional.empty();
        }
        Article article = optionalArticle.get();
        article.update(copyFrom);
        return Optional.of(articleRepository.save(article));
    }

    public Optional<Article> findById(String slugUuid) {
        UUID id = toUuid(slugUuid);
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return Optional.empty();
        }
        return optionalArticle;
    }

    public Page<Article> findByStatus(String status, Pageable pageable) {
        return this.articleRepository.findAllByStatus(
                ArticleStatus.valueOf(status.toUpperCase(Locale.ENGLISH)),
                pageable);
    }

    public Article publish(Article article) {
        article.setStatus(ArticleStatus.valueOf("PUBLISHED"));
        return this.articleRepository.save(article);
    }

    public boolean delete(String slugUuid) {
        UUID uuid = toUuid(slugUuid);
        Optional<Article> optionalArticle = articleRepository.findById(uuid);
        if (!optionalArticle.isPresent()) {
            return false;
        }
        optionalArticle.ifPresent(a -> articleRepository.deleteById(a.getId()));
        return true;
    }

    public Page<Article> findAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

}

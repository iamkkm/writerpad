package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.exception.ArticleNotFoundException;
import com.xebia.fs101.writerpad.exception.ArticleWithSameBodyException;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.xebia.fs101.writerpad.util.StringUtil.toUuid;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRenderService imageRenderService;
    @Autowired
    private CopyPasteDecoder copyPasteDecoder;

    public Article save(Article article, User user) {
        if (copyPasteDecoder.isCopied(article.getBody()))
            throw new ArticleWithSameBodyException();
        Optional<User> optionalUser = userRepository.findById(user.getId());
        User foundUser = optionalUser.get();
        article.setUser(foundUser);
        article.setImage(imageRenderService.imageRender());
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
        if (copyPasteDecoder.isCopied(copyFrom.getBody()))
            throw new ArticleWithSameBodyException();
        UUID id = toUuid(slugUuid);
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isPresent()) {
            return Optional.empty();
        }
        Article article = optionalArticle.get();
        article.update(copyFrom);
        return Optional.of(articleRepository.save(article));
    }

    public Article findById(String slugUuid) {
        UUID id = toUuid(slugUuid);
        return articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
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

    @Transactional(readOnly = true)
    public Map<String, Long> findTagsWithOccurence() {
        return this.articleRepository.findAllTags().collect(groupingBy(tag -> tag, counting()));
    }

    public void favoritedArticle(String slugUuid) {
        Article article = findById(slugUuid);
        article.markFavorited();
        articleRepository.save(article);
    }

    public void unfavoritedArticle(String slugUuid) {
        Article article = findById(slugUuid);
        article.markUnfavorited();
        articleRepository.save(article);

    }
}

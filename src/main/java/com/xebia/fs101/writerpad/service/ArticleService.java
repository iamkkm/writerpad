package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.xebia.fs101.writerpad.util.StringUtil.convertToLowercase;
import static com.xebia.fs101.writerpad.util.StringUtil.slug;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public boolean isValidArticalRequest(ArticleRequest articleRequest) {
        if (articleRequest.getTitle() == null || articleRequest.getTitle().equals("")
                || articleRequest.getDescription() == null || articleRequest
                .getDescription().equals("") || articleRequest.getBody() == null
                || articleRequest.getBody().equals(""))
            throw new IllegalArgumentException("Title"
                    + " or body or description is either null/empty.");
        else
            return true;
    }

    public Article saveArticle(ArticleRequest articleRequest) {
        Article article = new Article.Builder()
                .withSlug(slug(articleRequest.getTitle()))
                .withTitle(articleRequest.getTitle())
                .withDescription(articleRequest.getDescription())
                .withBody(articleRequest.getBody())
                .withTagList(convertToLowercase(articleRequest.getTags()))
                .withCreatedAt(new Date())
                .withUpdatedAt(new Date())
                .withFavorited(false)
                .withFavoriteCount(0)
                .build();
        articleRepository.save(article);
        return article;
    }
}

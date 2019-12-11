package com.xebia.fs101.writerpad.api.representations;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;

import java.util.Date;
import java.util.List;

public class ArticleResponse {
    private String id;
    private String title;
    private String slug;
    private String description;
    private String body;
    private List<String> tagList;
    private Date createdAt = new Date();
    private Date updatedAt;
    private boolean favorited;
    private long favoriteCount;
    private UserResponse author;
    private ArticleStatus status;

    private ArticleResponse(Builder builder) {
        id = builder.id;
        title = builder.title;
        slug = builder.slug;
        description = builder.description;
        body = builder.body;
        tagList = builder.tags;
        createdAt = builder.createdAt;
        updatedAt = builder.updatedAt;
        favorited = builder.favorited;
        favoriteCount = builder.favoriteCount;
        author = builder.author;
        status = builder.status;
    }

    public static ArticleResponse from(Article article) {
        return new ArticleResponse.Builder()
                .withId(article.getId().toString())
                .withBody(article.getBody())
                .withCreatedAt(article.getCreatedAt())
                .withUpdatedAt(article.getUpdatedAt())
                .withDescription(article.getDescription())
                .withSlug(article.getSlug())
                .withTags(article.getTags())
                .withTitle(article.getTitle())
                .withAuthor(new UserResponse(article.getUser().getUsername()))
                .withFavoritesCount(article.getFavoriteCount())
                .withFavorited(article.getFavorited())
                .withStatus(article.getStatus())
                .build();
    }

    private static class UserResponse {
        private String username;

        UserResponse(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public long getFavoriteCount() {
        return favoriteCount;
    }

    public UserResponse getAuthor() {
        return author;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public static final class Builder {
        private String id;
        private String title;
        private String slug;
        private String description;
        private String body;
        private List<String> tags;
        private Date createdAt;
        private Date updatedAt;
        private boolean favorited;
        private long favoriteCount;
        private UserResponse author;
        public ArticleStatus status;

        public Builder() {
        }

        public Builder withId(String val) {
            this.id = val;
            return this;
        }

        public Builder withTitle(String val) {
            title = val;
            return this;
        }

        public Builder withSlug(String val) {
            slug = val;
            return this;
        }

        public Builder withDescription(String val) {
            description = val;
            return this;
        }

        public Builder withBody(String val) {
            body = val;
            return this;
        }

        public Builder withTags(List<String> val) {
            tags = val;
            return this;
        }

        public Builder withCreatedAt(Date val) {
            createdAt = val;
            return this;
        }

        public Builder withUpdatedAt(Date val) {
            updatedAt = val;
            return this;
        }

        public Builder withFavorited(boolean val) {
            favorited = val;
            return this;
        }

        public Builder withFavoritesCount(long val) {
            favoriteCount = val;
            return this;
        }

        public Builder withAuthor(UserResponse val) {
            author = val;
            return this;
        }

        public Builder withStatus(ArticleStatus val) {
            status = val;
            return this;
        }

        public ArticleResponse build() {
            return new ArticleResponse(this);
        }
    }
}

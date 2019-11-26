package com.xebia.fs101.writerpad.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String slug;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String body;
    @ElementCollection
    private List<String> tagList;
    private Date createdAt;
    private Date updatedAt;
    private Boolean favorited;
    private int favoriteCount;

    private Article(Builder builder) {
        setId(builder.id);
        setSlug(builder.slug);
        setTitle(builder.title);
        setDiscription(builder.description);
        setBody(builder.body);
        setTagList(builder.tagList);
        setCreatedAt(builder.createdAt);
        setUpdatedAt(builder.updatedAt);
        setFavorited(builder.favorited);
        setFavoriteCount(builder.favoriteCount);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDiscription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getFavorited() {
        return favorited;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }


    public static final class Builder {
        private UUID id;
        private String slug;
        private @NotNull String title;
        private @NotNull String description;
        private @NotNull String body;
        private List<String> tagList;
        private Date createdAt;
        private Date updatedAt;
        private Boolean favorited;
        private int favoriteCount;

        public Builder() {
        }

        public Builder withId(UUID val) {
            id = val;
            return this;
        }

        public Builder withSlug(String val) {
            slug = val;
            return this;
        }

        public Builder withTitle(@NotNull String val) {
            title = val;
            return this;
        }

        public Builder withDescription(@NotNull String val) {
            description = val;
            return this;
        }

        public Builder withBody(@NotNull String val) {
            body = val;
            return this;
        }

        public Builder withTagList(List<String> val) {
            tagList = val;
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

        public Builder withFavorited(Boolean val) {
            favorited = val;
            return this;
        }

        public Builder withFavoriteCount(int val) {
            favoriteCount = val;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }

    @Override
    public String toString() {
        return "Article{"
                + "id=" + id
                + ", slug='" + slug + '\''
                + ", title='" + title + '\''
                + ", description='" + description + '\''
                + ", body='" + body + '\''
                + ", tagList=" + tagList
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", favorited=" + favorited
                + ", favoriteCount=" + favoriteCount
                + '}';
    }
}

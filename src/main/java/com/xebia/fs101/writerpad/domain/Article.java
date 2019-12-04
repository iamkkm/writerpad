package com.xebia.fs101.writerpad.domain;


import com.xebia.fs101.writerpad.util.StringUtil;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Transient
    private String slug;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String body;
    @ElementCollection
    private List<String> tagList;
    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    private boolean favorited;
    private int favoriteCount;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status = ArticleStatus.valueOf("DRAFT");

    @OneToMany(mappedBy = "article")
    private List<Comment> comments;

    public Article() {
    }


    public void setUpdatedAt() {
        this.updatedAt = new Date();
    }

    private Article(Builder builder) {
        id = builder.id;
        description = builder.description;
        title = builder.title;
        slug = StringUtil.slug(title);
        body = builder.body;
        tagList = builder.tagList;
        setUpdatedAt();
        favorited = false;
        favoriteCount = 0;
        status = ArticleStatus.DRAFT;
        comments = builder.comments;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getSlug() {
        return StringUtil.slug(this.title);
    }

    public String getTitle() {
        return title;
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

    public Boolean getFavorited() {
        return favorited;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public ArticleStatus getStatus() {
        return status;
    }


    public Article update(Article copyFrom) {
        if (Objects.nonNull(copyFrom.getTitle())) {
            this.title = copyFrom.getTitle();
        }
        if (Objects.nonNull(copyFrom.getBody())) {
            this.body = copyFrom.getBody();
        }
        if (Objects.nonNull(copyFrom.getDescription())) {
            this.description = copyFrom.getDescription();
        }
        this.updatedAt = new Date();
        return this;
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
        private ArticleStatus status;
        private List<Comment> comments;

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


        public Builder withFavorited(Boolean val) {
            favorited = val;
            return this;
        }

        public Builder withFavoriteCount(int val) {
            favoriteCount = val;
            return this;
        }

        public Builder withStatus(ArticleStatus val) {
            status = val;
            return this;
        }

        public Builder withComments(List<Comment> val) {
            comments = val;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }
}

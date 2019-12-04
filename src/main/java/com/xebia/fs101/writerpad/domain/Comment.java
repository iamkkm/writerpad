package com.xebia.fs101.writerpad.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @NotBlank
    private String body;
    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public Comment() {
    }

    private Comment(Builder builder) {
        id = builder.id;
        createdAt = builder.createdAt;
        updatedAt = builder.updatedAt;
        body = builder.body;
        ipAddress = builder.ipAddress;
        article = builder.article;
    }

    public Long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getBody() {
        return body;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static final class Builder {
        private Long id;
        private Date createdAt;
        private Date updatedAt;
        private @NotBlank String body;
        private String ipAddress;
        private Article article;

        public Builder() {
        }

        public Builder withId(Long val) {
            id = val;
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

        public Builder withBody(@NotBlank String val) {
            body = val;
            return this;
        }

        public Builder withIpAddress(String val) {
            ipAddress = val;
            return this;
        }

        public Builder withArticle(Article val) {
            article = val;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }

    @Override
    public String toString() {
        return "Comments{"
                + "id=" + id
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + ", body='" + body + '\''
                + ", ipAddress='" + ipAddress + '\''
                + ", article=" + article
                + '}';
    }
}

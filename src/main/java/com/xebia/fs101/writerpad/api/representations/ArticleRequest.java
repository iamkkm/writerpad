package com.xebia.fs101.writerpad.api.representations;

import com.xebia.fs101.writerpad.domain.Article;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;

public class ArticleRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String body;
    private String[] tags;

    public ArticleRequest() {
    }

    public Article toArticle() {
        return new Article.Builder().withTitle(this.getTitle())
                .withDescription(this.getDescription())
                .withBody(this.getBody())
                .withTags(this.tags == null ? new ArrayList<>() : Arrays.asList(this.tags))
                .build();
    }

    private ArticleRequest(Builder builder) {
        title = builder.title;
        description = builder.description;
        body = builder.body;
        tags = builder.tags;
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

    public String[] getTags() {
        return tags;
    }


    public static final class Builder {
        private String title;
        private String description;
        private String body;
        private String[] tags;

        public Builder() {
        }

        public Builder withTitle(String val) {
            title = val;
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

        public Builder withTags(String[] val) {
            tags = val;
            return this;
        }

        public ArticleRequest build() {
            return new ArticleRequest(this);
        }
    }

    @Override
    public String toString() {
        return "ArticleRequest{"
                + "title='" + title + '\''
                + ", description='" + description + '\''
                + ", body='" + body + '\''
                + ", tags[]='" + tags + '\''
                + '}';
    }
}

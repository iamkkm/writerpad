package com.xebia.fs101.writerpad.request;

public class ArticleRequest {

    private String title;
    private String description;
    private String body;
    private String[] tags;

    public ArticleRequest() {
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

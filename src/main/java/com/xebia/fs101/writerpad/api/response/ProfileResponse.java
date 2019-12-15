package com.xebia.fs101.writerpad.api.response;

import com.xebia.fs101.writerpad.domain.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProfileResponse {

    private String username;
    private boolean following;
    private int followerCount;
    private int followingCount;
    private List<ArticleResponse> articles;

    private ProfileResponse(Builder builder) {
        username = builder.username;
        following = builder.following;
        followerCount = builder.followerCount;
        followingCount = builder.followingCount;
        articles = builder.articles;
    }

    public String getUsername() {
        return username;
    }

    public boolean isFollowing() {
        return following;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public List<ArticleResponse> getArticles() {
        return articles;
    }

    public static ProfileResponse from(User user) {
        return new Builder()
                .withUsername(user.getUsername())
                .withFollowing(user.isFollowing())
                .withFollowerCount(user.getFollowerCount())
                .withFollowingCount(user.getFollowerCount())
                .withArticles(user.getArticles().stream()
                        .map(article -> new ArticleResponse(article.getId(), article.getTitle()))
                        .collect(Collectors.toList()))
                .build();
    }

    private static class ArticleResponse {
        private UUID id;
        private String title;

        ArticleResponse(UUID id, String title) {
            this.id = id;
            this.title = title;
        }

        public UUID getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }


    public static final class Builder {
        private String username;
        private boolean following;
        private int followerCount;
        private int followingCount;
        private List<ArticleResponse> articles;

        public Builder() {
        }

        public Builder withUsername(String val) {
            username = val;
            return this;
        }

        public Builder withFollowing(boolean val) {
            following = val;
            return this;
        }

        public Builder withFollowerCount(int val) {
            followerCount = val;
            return this;
        }

        public Builder withFollowingCount(int val) {
            followingCount = val;
            return this;
        }

        public Builder withArticles(List<ArticleResponse> val) {
            articles = val;
            return this;
        }

        public ProfileResponse build() {
            return new ProfileResponse(this);
        }
    }
}

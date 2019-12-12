package com.xebia.fs101.writerpad.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @NotBlank
    private String password;
    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<Article> articles;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public User() {
    }


    private User(Builder builder) {
        id = builder.id;
        username = builder.username;
        email = builder.email;
        password = builder.password;
        role = builder.role;
    }

    public User(User other) {
        this.id = other.id;
        this.username = other.username;
        this.email = other.email;
        this.password = other.password;
        this.role = other.role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public UserRole getRole() {
        return role;
    }

    public static final class Builder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private UserRole role;

        public Builder() {
        }

        public Builder withId(Long val) {
            id = val;
            return this;
        }

        public Builder withUsername(String val) {
            username = val;
            return this;
        }

        public Builder withEmail(String val) {
            email = val;
            return this;
        }

        public Builder withPassword(String val) {
            password = val;
            return this;
        }

        public Builder withRole(UserRole val) {
            role = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{"
                + "username='" + username + '\''
                + ", email='" + email + '\''
                + '}';
    }
}

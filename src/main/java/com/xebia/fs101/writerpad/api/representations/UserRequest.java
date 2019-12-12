package com.xebia.fs101.writerpad.api.representations;

import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.domain.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private UserRole role;

    public UserRequest() {
    }

    private UserRequest(Builder builder) {
        username = builder.username;
        email = builder.email;
        password = builder.password;
        role = builder.role;
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

    public UserRole getRole() {
        return role;
    }

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User.Builder()
                .withUsername(this.getUsername())
                .withEmail(this.getEmail())
                .withPassword(passwordEncoder.encode(password))
                .withRole(this.getRole())
                .build();
    }

    public static final class Builder {
        private @NotBlank String username;
        private @NotBlank @Email String email;
        private @NotBlank String password;
        private @NotBlank UserRole role;

        public Builder() {
        }

        public Builder withUsername(@NotBlank String val) {
            username = val;
            return this;
        }

        public Builder withEmail(@NotBlank @Email String val) {
            email = val;
            return this;
        }

        public Builder withPassword(@NotBlank String val) {
            password = val;
            return this;
        }

        public Builder withRole(@NotBlank UserRole val) {
            role = val;
            return this;
        }

        public UserRequest build() {
            return new UserRequest(this);
        }
    }
}

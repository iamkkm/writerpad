package com.xebia.fs101.writerpad.api.representations;

import com.xebia.fs101.writerpad.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;

    public UserRequest() {
    }

    private UserRequest(Builder builder) {
        username = builder.username;
        email = builder.email;
        password = builder.password;
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

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User.Builder()
                .withUsername(this.getUsername())
                .withEmail(this.getEmail())
                .withPassword(passwordEncoder.encode(password))
                .build();
    }

    public static final class Builder {
        private @NotBlank String username;
        private @NotBlank @Email String email;
        private @NotBlank String password;

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

        public UserRequest build() {
            return new UserRequest(this);
        }
    }
}

package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.api.representations.UserRequest;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.service.UserService;
import com.xebia.fs101.writerpad.service.security.AdminOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @AdminOnly
    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody UserRequest userRequest) {
        User user = userService.save(userRequest.toUser(passwordEncoder));
        return new ResponseEntity<>(user, CREATED);
    }

    @GetMapping
    public String getMethod() {
        return "abc";
    }

    @ResponseStatus(value = BAD_REQUEST,
            reason = "User exist")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void badRequest() {
        //do nothing
    }
}

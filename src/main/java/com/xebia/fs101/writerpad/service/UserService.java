package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }
}

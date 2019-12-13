package com.xebia.fs101.writerpad.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.fs101.writerpad.api.representations.UserRequest;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.domain.UserRole;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserResourceTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        user = new User.Builder()
                .withUsername("testadmin")
                .withEmail("testadmin@mail.com")
                .withPassword(passwordEncoder.encode("abc"))
                .withRole(UserRole.ADMIN)
                .build();
        userRepository.save(user);
    }

    @Test
    void should_save_new_user_all_values_given_correct() throws Exception {
        UserRequest userRequest = new UserRequest.Builder()
                .withUsername("kk")
                .withEmail("kk@gmail.com")
                .withPassword("abc")
                .withRole(UserRole.WRITER)
                .build();
        String user = objectMapper.writeValueAsString(userRequest);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user)
                .with(httpBasic("testadmin", "abc")))
                .andDo(print())
                .andExpect(status().isCreated());
        List<User> allUsers = userRepository.findAll();
        Assertions.assertThat(allUsers.size()).isGreaterThan(0);
    }

    @Test
    void should_not_create_user_when_user_already_exist() throws Exception {
        UserRequest userRequest1 = new UserRequest.Builder()
                .withUsername("kk")
                .withEmail("kk@kk.com")
                .withPassword("abc")
                .withRole(UserRole.WRITER)
                .build();
        UserRequest userRequest2 = new UserRequest.Builder()
                .withUsername("kk")
                .withEmail("kk@kk.com")
                .withPassword("abc")
                .withRole(UserRole.WRITER)
                .build();
        String user1 = objectMapper.writeValueAsString(userRequest1);
        String user2 = objectMapper.writeValueAsString(userRequest2);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user1)
                .with(httpBasic("testadmin", "abc")))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user2)
                .with(httpBasic("testadmin", "abc")))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
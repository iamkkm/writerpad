package com.xebia.fs101.writerpad;

import com.xebia.fs101.writerpad.repository.ArticleRepository;
import com.xebia.fs101.writerpad.repository.CommentRepository;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WriterpadApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ArticleRepository articleRepository;

	@Test
	void contextLoads() {
		commentRepository.deleteAll();
		articleRepository.deleteAll();
		userRepository.deleteAll();
	}

}

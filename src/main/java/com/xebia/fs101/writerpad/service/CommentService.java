package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SpamChecker spamChecker;

    public Optional<Comment> save(Comment comments) throws IOException {
        if (spamChecker.isSpam(comments.getBody())) {
            return Optional.empty();
        } else {
            return Optional.of(commentRepository.save(comments));
        }
    }

    public List<Comment> findByArticleId(UUID id) {
        return commentRepository.findAllCommentsByArticleId(id);
    }

    public void delete(long id) {
        commentRepository.deleteById(id);
    }
}

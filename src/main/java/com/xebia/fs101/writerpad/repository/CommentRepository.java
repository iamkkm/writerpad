package com.xebia.fs101.writerpad.repository;

import com.xebia.fs101.writerpad.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllCommentsByArticleId(UUID uuid);
}

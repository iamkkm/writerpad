package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private SpamChecker spamChecker;
    @InjectMocks
    private CommentService commentService;

    @Test
    void should_be_able_to_save_a_comment() throws IOException {
        Comment comment = new Comment.Builder()
                .withBody("comment")
                .build();
        when(commentRepository.save(comment)).thenReturn(comment);
        commentService.save(comment);
        verify(commentRepository).save(comment);
        Mockito.verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void should_be_able_to_find_all_comments_by_article_id() {
        UUID id = UUID.randomUUID();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        Comment comment3 = new Comment();
        List<Comment> comments = Arrays.asList(comment1, comment2, comment3);
        when(commentRepository.findAllCommentsByArticleId(id)).thenReturn(comments);
        List<Comment> saved = commentService.findByArticleId(id);
        assertThat(saved).hasSize(3);
        verify(commentRepository).findAllCommentsByArticleId(id);
        Mockito.verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void should_be_able_to_delete_a_comment() {
        Comment comment = new Comment();
        comment.setId(1L);
        commentService.delete(comment.getId());
        verify(commentRepository).deleteById(any());
        Mockito.verifyNoMoreInteractions(commentRepository);
    }
}

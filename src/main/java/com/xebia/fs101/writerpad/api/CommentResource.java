package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.api.representations.CommentRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.Comment;
import com.xebia.fs101.writerpad.service.ArticleService;
import com.xebia.fs101.writerpad.service.CommentService;
import com.xebia.fs101.writerpad.service.SpamChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
public class CommentResource {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SpamChecker spamChecker;

    @PostMapping("/{slugUuid}/comments")
    public ResponseEntity<Comment> post(@Valid @RequestBody CommentRequest commentRequest,
                                        @PathVariable("slugUuid") String slugUuid,
                                        HttpServletRequest request) throws IOException {
        Optional<Article> article = articleService.findArticleById(slugUuid);
        if (article.isPresent()) {
            Article found = article.get();
            Comment toSave = commentRequest.toComment(found, request.getRemoteAddr());
            Optional<Comment> saved = commentService.save(toSave);
            return saved.map(comment -> ResponseEntity.status(HttpStatus.CREATED)
                    .body(comment)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
    }

    @GetMapping("/{slugUuid}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String slugUuid) {
        Article article = articleService.findById(slugUuid);
        UUID id = article.getId();
        List<Comment> comments = commentService.findByArticleId(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{slugUuid}/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String slugUuid,
                                              @PathVariable long id) {
        Article article = articleService.findById(slugUuid);
        commentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

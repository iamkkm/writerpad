package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.api.representations.ArticleRequest;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.exception.WriterpadException;
import com.xebia.fs101.writerpad.service.ArticleService;
import com.xebia.fs101.writerpad.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/articles")
public class ArticleResource {

    @Autowired
    private ArticleService articleService;

    @Autowired
    EmailService emailService;

    @PostMapping
    public ResponseEntity<Article> save(@Valid @RequestBody ArticleRequest articleRequest) {
        Article article = articleService.save(articleRequest.toArticle());
        return new ResponseEntity<>(article, CREATED);
    }

    @PatchMapping(path = "/{slug_uuid}")
    public ResponseEntity<Article> update(@RequestBody ArticleRequest articleRequest,
                                          @PathVariable("slug_uuid") String slugUuid) {
        Article copyFrom = articleRequest.toArticle();
        Optional<Article> updatedArticle = articleService.update(slugUuid, copyFrom);
        return updatedArticle.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{slugUuid}")
    public ResponseEntity<Article> getArticleById(@PathVariable("slugUuid") String slugUuid) {
        Optional<Article> article = articleService.findArticleById(slugUuid);
        if (article.isPresent()) {
            Article found = article.get();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(found);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Article>> listAllArticles(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Article> pageResult = articleService.findAllArticles(pageable);
        List<Article> found = pageResult.getContent();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(found);
    }

    @DeleteMapping(path = "/{slug_uuid}")
    public ResponseEntity<Void> delete(@PathVariable("slug_uuid") String slugUuid) {
        boolean deleted = articleService.delete(slugUuid);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<Article>> getByStatus(
            @RequestParam("status") String status,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Article> pageResult = articleService.findByStatus(status, pageable);
        List<Article> found = pageResult.getContent();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(found);
    }

    @PostMapping("/{slugUuid}/PUBLISH")
    public ResponseEntity<Void> publish(@PathVariable("slugUuid") String slugUuid) {
        Optional<Article> article = articleService.findArticleById(slugUuid);
        if (article.isPresent() && article.get().getStatus() == ArticleStatus.DRAFT) {
            Article published = articleService.publish(article.get());
            try {
                emailService.sendMail(published);
            } catch (Exception ex) {
                throw new WriterpadException(published, ex);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } else if (article.isPresent() && article.get().getStatus() == ArticleStatus.PUBLISHED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
    }

}

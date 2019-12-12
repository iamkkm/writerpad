package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.api.representations.ArticleRequest;
import com.xebia.fs101.writerpad.api.response.ArticleResponse;
import com.xebia.fs101.writerpad.api.response.ReadTimeResponse;
import com.xebia.fs101.writerpad.api.response.TagResponse;
import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import com.xebia.fs101.writerpad.domain.ReadTime;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.exception.WriterpadException;
import com.xebia.fs101.writerpad.service.ArticleService;
import com.xebia.fs101.writerpad.service.EmailService;
import com.xebia.fs101.writerpad.service.ReadTimeCalculator;
import com.xebia.fs101.writerpad.service.security.EditorOnly;
import com.xebia.fs101.writerpad.service.security.WriterOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/api/articles")
public class ArticleResource {

    @Autowired
    private ArticleService articleService;

    @Autowired
    EmailService emailService;

    @Autowired
    private ReadTimeCalculator readTimeCalculator;

    @WriterOnly
    @PostMapping
    public ResponseEntity<ArticleResponse> save(@AuthenticationPrincipal User user,
            @Valid @RequestBody ArticleRequest articleRequest) {
        ArticleResponse articleResponse = ArticleResponse.from(
                articleService.save(articleRequest.toArticle(), user));
        return new ResponseEntity<>(articleResponse, CREATED);
    }

    @PatchMapping(path = "/{slug_uuid}")
    public ResponseEntity<Article> update(@AuthenticationPrincipal User user,
                                          @RequestBody ArticleRequest articleRequest,
                                          @PathVariable("slug_uuid") String slugUuid) {
        Article article = articleService.findById(slugUuid);
        if (!article.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
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
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user,
                                       @PathVariable("slug_uuid") String slugUuid) {
        Article article = articleService.findById(slugUuid);
        if (!article.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(FORBIDDEN).build();
        }
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

    @EditorOnly
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
    @GetMapping(path = "/{slugUuid}/timetoread")
    public ResponseEntity<ReadTimeResponse> timeToRead(
            @PathVariable("slugUuid") final String slugUuid) {
        Article foundArticle = this.articleService.findById(slugUuid);
        Duration duration = readTimeCalculator.calculateReadingTime(foundArticle.getBody());
        ReadTimeResponse readTimeResponse = new ReadTimeResponse(
                slugUuid, new ReadTime((int) duration.toMinutes(),
                (int) duration.getSeconds() % 60));
        return new ResponseEntity<>(readTimeResponse, HttpStatus.OK);

    }

    @GetMapping(path = "/tags")
    public ResponseEntity<List<TagResponse>> getTagsWithOccurence() {
        Map<String, Long> tagsWithOccurence = this.articleService.findTagsWithOccurence();
        List<TagResponse> tagResponses = tagsWithOccurence.entrySet().stream()
                .map(e -> new TagResponse(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(tagResponses, HttpStatus.OK);

    }

    @PutMapping(path = "/{slug_uuid}/favorited")
    public ResponseEntity<Void> favoriteArticle(@PathVariable("slug_uuid") String slugUuid) {
        this.articleService.favoritedArticle(slugUuid);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{slug_uuid}/unfavorited")
    public ResponseEntity<Void> unfavoriteArticle(@PathVariable("slug_uuid") String slugUuid) {
        this.articleService.unfavoritedArticle(slugUuid);
        return ResponseEntity.noContent().build();
    }
}

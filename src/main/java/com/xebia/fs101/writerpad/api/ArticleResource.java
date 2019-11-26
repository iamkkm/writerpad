package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.request.ArticleRequest;
import com.xebia.fs101.writerpad.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/articles")
public class ArticleResource {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> saveArticle(@RequestBody ArticleRequest articleRequest) {
        boolean articleValidity = false;
        try {
            articleValidity = articleService.isValidArticalRequest(articleRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        if (articleValidity) {
            Article article = articleService.saveArticle(articleRequest);
            return new ResponseEntity<>(article, CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@PostMapping
    public ResponseEntity<Void> create(){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }*/

}

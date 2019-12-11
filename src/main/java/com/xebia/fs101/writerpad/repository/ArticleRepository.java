package com.xebia.fs101.writerpad.repository;

import com.xebia.fs101.writerpad.domain.Article;
import com.xebia.fs101.writerpad.domain.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Page<Article> findAllByStatus(ArticleStatus status, Pageable pageable);

    Article save(Article article);

    @Query(value = "select TAGS from ARTICLE_TAGS", nativeQuery = true)
    Stream<String> findAllTags();
}

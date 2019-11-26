package com.xebia.fs101.writerpad.repository;

import com.xebia.fs101.writerpad.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}

package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.repository.ArticleRepository;
import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CopyPasteDecoder {

    @Autowired
    private ArticleRepository articleRepository;

    public double findIndex(String target, String source) {
        SimilarityStrategy strategy = new JaroWinklerStrategy();
        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
        return service.score(source, target);
    }

    public boolean isCopied(String target) {
        return articleRepository.findAll()
                .stream()
                .anyMatch(article -> findIndex(target, article.getBody()) > 0.7);
    }
}

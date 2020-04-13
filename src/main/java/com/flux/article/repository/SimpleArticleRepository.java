package com.flux.article.repository;

import com.flux.article.model.Article;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface SimpleArticleRepository extends R2dbcRepository<Article, Long> {

    @Query("SELECT article " +
            "FROM Article article " +
            "WHERE OFFSET :offset LIMIT :limit" +
            "ORDER BY article.idx desc")
    Flux<Article> findAll(int offset, int limit);
}

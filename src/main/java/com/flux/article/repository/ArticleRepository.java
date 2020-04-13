package com.flux.article.repository;

import com.flux.article.model.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ArticleRepository {

    private final DatabaseClient databaseClient;

    public Flux<Article> findAll(Pageable pageable){

        return databaseClient
                .select()
                .from(Article.class)
                .page(pageable)
                .fetch()
                .all();
    }

    public Mono<Article> findByIdx(Long articleIdx){

        return databaseClient
                .select()
                .from(Article.class)
                .matching(Criteria.where("idx").is(articleIdx))
                .fetch()
                .one()
                .switchIfEmpty(
                    Mono.error(new RuntimeException("not found "+articleIdx))
                );
    }

    public void create(Article article){

        Mono<Map<String, Object>> result = databaseClient.insert()
                .into(Article.class)
                .using(article)
                .fetch().one();
    }
}

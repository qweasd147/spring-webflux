package com.flux.article.repository;

import com.flux.article.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static org.springframework.data.r2dbc.query.Criteria.where;

@RequiredArgsConstructor
@Repository
@Slf4j
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

    public Mono<Long> getTotalArticleCount(){

        return databaseClient.execute("SELECT COUNT(*) FROM article")
                .map((row, metadata) -> row.get(0, Long.class)) //
                .first()
                .defaultIfEmpty(0L);
    }


    public Mono<Article> findByIdx(Long articleIdx){

        return databaseClient
                .select()
                .from(Article.class)
                .matching(where("idx").is(articleIdx))
                .fetch()
                .one()
                .switchIfEmpty(
                    Mono.error(new RuntimeException("not found "+articleIdx))
                );
    }

    public void create(Article article){

        Mono<Void> result2 = databaseClient.insert()
                .into(Article.class)
                .using(article)
                .then();

        log.info("insert success");
    }

    public void update(Article article){

        Mono<Void> result = databaseClient.update()
                .table(Article.class)
                .using(article)
                .then();

        log.info("update success");
    }

    public void delete(Long articleIdx){

        Mono<Void> result = databaseClient.delete()
                .from(Article.class)
                .matching(where("idx").is(articleIdx))
                .then();

        log.info("delete success");
    }
}

package com.flux.article.repository;

import com.flux.article.model.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static org.springframework.data.r2dbc.query.Criteria.where;

@RequiredArgsConstructor
@Repository
@Slf4j
@Transactional
public class ArticleRepository {

    private final DatabaseClient databaseClient;

    public Flux<Article> findAll(){

        return databaseClient
                .select()
                .from(Article.class)
                .fetch()
                .all();
    }

    public Flux<Article> findAll(Pageable pageable){

        return databaseClient
                .select()
                .from(Article.class)
                .page(pageable)
                .fetch()
                .all()
                .log();
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

    public Mono<Article> create(Article article){

        databaseClient.insert()
                .into(Article.class)
                .using(article)
                .fetch().one().subscribe();

        return Mono.just(article);
    }


    public void update(Article article){

        databaseClient.update()
                .table(Article.class)
                .using(article)
                .fetch()
                .rowsUpdated();

        log.info("update success");

        Mono.empty();
    }

    public Mono<Void> delete(Long articleIdx){

        databaseClient.delete()
                .from(Article.class)
                .matching(where("idx").is(articleIdx))
                .fetch().rowsUpdated();

        return Mono.empty();
    }
}

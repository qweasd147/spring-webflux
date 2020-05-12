package com.flux.article.service;

import com.flux.article.model.Article;
import com.flux.article.model.ArticleDto;
import com.flux.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;



@Service
@RequiredArgsConstructor
@Transactional
public class ArticleHandler {

    private final ArticleRepository articleRepository;

    public Mono<ServerResponse> findAll(ServerRequest request){

        return ServerResponse.ok().body(articleRepository.findAll(), Article.class);
    }

    public Mono<Page<Article>> findAllToPage(ServerRequest request) {

        return Mono.just(request.queryParams())
                .map((params)->{

                    String page = params.getOrDefault("page", Arrays.asList("1")).get(0);
                    String size = params.getOrDefault("size", Arrays.asList("10")).get(0);
                    String sort = params.getOrDefault("sort", Arrays.asList("idx")).get(0);

                    return PageRequest.of(Integer.parseInt(page)
                            , Integer.parseInt(size)
                            , Sort.Direction.DESC, sort);
                })
                .flatMap(pageRequest -> {
                    Flux<Article> pageContents = articleRepository.findAll(pageRequest);
                    Mono<Long> totalCount = articleRepository.getTotalArticleCount();
                    List<Article> list = pageContents.collectList().block();

                    PageImpl<Article> articlesWithPage = new PageImpl<>(list, pageRequest, totalCount.block());

                    return Mono.just(articlesWithPage);
                });
    }

    public Mono<ServerResponse> findOne(ServerRequest request){

        return Mono.just(request.pathVariable("articleIdx"))
                .map(Long::parseLong)
                .flatMap(articleIdx -> articleRepository.findByIdx(articleIdx))
                .flatMap(article-> ServerResponse.ok().body(Mono.just(article), Article.class))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> createOne(ServerRequest serverRequest){

        return serverRequest.bodyToMono(ArticleDto.CreateRequest.class)
                .flatMap(createRequest ->
                        this.articleRepository.create(createRequest.toEntity()))
                .flatMap(article
                        -> ServerResponse.created(URI.create("/articles/" + article.getIdx())).build());
    }

    public Mono<ServerResponse> deleteOne(ServerRequest serverRequest){

        return Mono.just(serverRequest.pathVariable("articleIdx"))
                .map(Long::parseLong)
                .flatMap(articleIdx -> this.articleRepository.delete(articleIdx))
                .flatMap((ignore)-> ServerResponse.noContent().build());
    }
}

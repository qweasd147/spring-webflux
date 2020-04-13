package com.flux.article.service;

import com.flux.article.model.Article;
import com.flux.article.model.ArticleDto;
import com.flux.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Flux<Article> findAll(Pageable pageable){
        return articleRepository.findAll(pageable);
    }

    public Mono<Article> findOne(Long articleIdx){

        return articleRepository.findByIdx(articleIdx);
    }

    public void createOne(ArticleDto.CreateRequest createRequest){

        articleRepository.create(createRequest.toEntity());
    }
}

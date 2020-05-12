package com.flux.article.service;

import com.flux.article.model.Article;
import com.flux.article.model.ArticleDto;
import com.flux.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    public Flux<Article> findAll(){
        return articleRepository.findAll();
    }

    public Mono<Page<Article>> findAllToPage(Pageable pageable) {

        return articleRepository.findAll(pageable)
                .collectList()
                .flatMap((findList) -> articleRepository.getTotalArticleCount()
                        .map((totalCount) -> new PageImpl<>(findList, pageable, totalCount))
                );
    }

    public Mono<Article> findOne(Long articleIdx){

        return articleRepository.findByIdx(articleIdx);
    }

    public void createOne(ArticleDto.CreateRequest createRequest){

        articleRepository.create(createRequest.toEntity());
    }
}

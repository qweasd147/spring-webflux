package com.flux.api.article;


import com.flux.article.model.Article;
import com.flux.article.model.ArticleDto;
import com.flux.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public Flux<Article> findAll(Pageable pageable){
        return articleService.findAll(pageable);
    }

    @GetMapping("/{articleIdx}")
    public Mono<Article> findOne(@PathVariable Long articleIdx){
        return articleService.findOne(articleIdx);
    }

    @PostMapping
    public Mono<Void> create(ArticleDto.CreateRequest createRequest){

        articleService.createOne(createRequest);

        return Mono.empty();
    }
}

package com.flux.api.article;

import com.flux.article.service.ArticleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FunctionalRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(ArticleHandler articleHandler){

        return route(GET("/api2/articles"), articleHandler::findAll)
                .andRoute(GET("/api2/articles/{articleId}"), articleHandler::findOne)
                .andRoute(POST("/api2/articles"), articleHandler::createOne)
                .andRoute(DELETE("/api2/articles"), articleHandler::deleteOne);
    }

}

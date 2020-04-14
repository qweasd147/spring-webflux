package com.flux.article.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotBlank;

public class ArticleDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class findAllRequest {

        private int page = 0;
        private int size = 16;
        private String sort = "idx";


        public Pageable toPageable(){
            return PageRequest.of(this.page,this.size, Sort.Direction.DESC, sort);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateRequest {

        @NotBlank(message = "제목 필수 입력")
        private String subject;

        @NotBlank(message = "내용 필수 입력")
        private String contents;

        @Builder
        public CreateRequest(String subject, String contents) {
            this.subject = subject;
            this.contents = contents;
        }

        public Article toEntity(){

            return Article.builder()
                    .subject(this.subject)
                    .contents(this.contents)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateRequest {

        @NotBlank(message = "제목 필수 입력")
        private String subject;

        @NotBlank(message = "내용 필수 입력")
        private String contents;

        @Builder
        public UpdateRequest(String subject, String contents) {
            this.subject = subject;
            this.contents = contents;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {

        private Long idx;
        private String subject;
        private String contents;

        @Builder
        private Res(Long idx, String subject, String contents) {
            this.idx = idx;
            this.subject = subject;
            this.contents = contents;
        }

        public static Res of(Article article){

            return Res.builder()
                    .idx(article.getIdx())
                    .subject(article.getSubject())
                    .contents(article.getContents())
                    .build();
        }
    }
}

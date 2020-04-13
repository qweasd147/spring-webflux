package com.flux.article.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class Article {

    @Id
    private Long idx;

    @Column
    @NotNull
    private String subject;

    @Column
    @NotNull
    private String contents;

    private LocalDateTime createdDate;

    @Builder
    private Article(@NotNull String subject, @NotNull String contents) {
        this.subject = subject;
        this.contents = contents;
        this.createdDate = LocalDateTime.now();
    }
}

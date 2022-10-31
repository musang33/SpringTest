package com.example.springtest.web.dto;

import com.example.springtest.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(final Posts entity) {
        id = entity.getId();
        title = entity.getTitle();
        author = entity.getAuthor();
        content = entity.getContent();
        modifiedDate = entity.getModifiedDate();
    }
}

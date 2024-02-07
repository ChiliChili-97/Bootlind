package com.sparta.bootlind.dto.responseDto;

import com.sparta.bootlind.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private String postTitle;
    private String content;
    private String username;
    private LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.postTitle = comment.getPost().getTitle();
        this.content =comment.getContent();
        this.username = comment.getUser().getUsername();
        this.createdAt =comment.getCreatedAt();
    }
}

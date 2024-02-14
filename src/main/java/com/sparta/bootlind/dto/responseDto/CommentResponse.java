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
    private String nickname;
    private LocalDateTime createdAt;
    private Integer likescnt;

    public CommentResponse(Comment comment, String nickname) {
        this.id = comment.getId();
        this.postTitle = comment.getPost().getTitle();
        this.content = comment.getContent();
        this.nickname = nickname;
        this.createdAt = comment.getCreatedAt();
        this.likescnt = comment.getLikescnt();
    }
}

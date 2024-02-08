package com.sparta.bootlind.entity;

import com.sparta.bootlind.dto.requestDto.CommentRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Comment extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String likes;

    @Column
    private Integer likescnt;



    public Comment(User user, Post post, CommentRequest request){
        this.user = user;
        this.post = post;
        this.content = request.getContent();
        this.likes = "";
    }

    public void update(CommentRequest request) {
        this.content = request.getContent();
    }

    public void updateUser(User blank) {
        this.user = blank;
    }
}

package com.sparta.bootlind.repository;

import com.sparta.bootlind.entity.Comment;
import com.sparta.bootlind.entity.Post;
import com.sparta.bootlind.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUser(User target);

    List<Comment> findAllByPost(Post post);
}

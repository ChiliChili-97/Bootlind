package com.sparta.bootlind.repository;

import com.sparta.bootlind.entity.Post;
import com.sparta.bootlind.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUser(User user);

    Optional<Object> findByTitle(String title);

    Optional<Object> findByCategory(String category);
}

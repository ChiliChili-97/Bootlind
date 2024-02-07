package com.sparta.bootlind.controller;

import com.sparta.bootlind.dto.requestDto.PostRequest;
import com.sparta.bootlind.dto.responseDto.PostResponse;
import com.sparta.bootlind.security.UserDetailsImpl;
import com.sparta.bootlind.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    @Operation(summary = "게시글 작성", description = "게시글을 작성한다.")
    public PostResponse createPost(@RequestBody PostRequest postRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequest, userDetails.getUser());
    }

    @GetMapping("/posts")
    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회한다.")
    public List<PostResponse> getPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostList(userDetails.getUser());
    }

    @GetMapping("/posts/{id}")
    @Operation(summary = "게시글 조회 (ID)", description = "사용자 ID를 사용해 게시글을 조회한다.")
    public PostResponse getPostById(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostById(id, userDetails.getUser());
    }
    @GetMapping("/posts/{title}")
    @Operation(summary = "게시글 조회 (제목)", description = "제목을 사용하여 게시글을 조회한다.")
    public PostResponse getPostByTitle(@PathVariable String title, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostByTitle(title, userDetails.getUser());
    }

    @GetMapping("/posts/{category}")
    @Operation(summary = "게시글 목록 조회 (카테고리)", description = "카테고리를 사용하여 게시글을 조회한다.")
    public List<PostResponse> getPostByCategory(@PathVariable String category, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostByCategory(category, userDetails.getUser());
    }

    @PutMapping("/posts/{id}")
    @Operation(summary = "게시글 수정", description = "지정된 ID를 사용하여 게시글을 수정한다.")
    public PostResponse updatePost(@PathVariable Long id,@RequestBody PostRequest postRequest , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequest, userDetails.getUser());
    }

    @DeleteMapping("/posts/{id}")
    @Operation(summary = "게시글 삭제", description = "지정된 ID를 사용하여 게시글을 삭제한다.")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser());
    }
}


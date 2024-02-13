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

    @GetMapping("/posts/{postid}")
    @Operation(summary = "게시글 조회 (게시글 ID)", description = "게시글 ID를 사용해 게시글을 조회한다.")
    public PostResponse getPostByPostId(@PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostByPostId(postid,userDetails.getUser());
    }

    @GetMapping("/posts/users/{nickname}")
    @Operation(summary = "게시글 조회 (사용자 Nickname)", description = "사용자 Nickname을 사용해 게시글을 조회한다.")
    public List<PostResponse> getPostByUserNickname(@PathVariable String nickname, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostByUserNickname(nickname, userDetails.getUser());
    }

    @GetMapping("/posts/titles/{title}")
    @Operation(summary = "게시글 조회 (제목)", description = "제목을 사용하여 게시글을 조회한다.")
    public PostResponse getPostByTitle(@PathVariable String title, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostByTitle(title, userDetails.getUser());
    }

    @GetMapping("/posts/titles/all/{title}")
    @Operation(summary = "게시글 목록 조회 (제목)", description = "제목을 사용하여 게시글 목록을 조회한다.")
    public List<PostResponse> getPostListByTitle(@PathVariable String title, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostListByTitle(title, userDetails.getUser());
    }

    @GetMapping("/posts/categories/{category}")
    @Operation(summary = "게시글 목록 조회 (카테고리)", description = "카테고리를 사용하여 게시글을 조회한다.")
    public List<PostResponse> getPostListByCategory(@PathVariable String category, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostListByCategory(category, userDetails.getUser());
    }

    @PutMapping("/posts/{postid}")
    @Operation(summary = "게시글 수정", description = "지정된 ID를 사용하여 게시글을 수정한다.")
    public PostResponse updatePost(@PathVariable Long postid, @RequestBody PostRequest postRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postid, postRequest, userDetails.getUser());
    }

    @DeleteMapping("/posts/{postid}")
    @Operation(summary = "게시글 삭제", description = "지정된 ID를 사용하여 게시글을 삭제한다.")
    public String deletePost(@PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postid, userDetails.getUser());
    }

    @PostMapping("/posts/likes/{postid}")
    @Operation(summary = "게시글 좋아요", description = "게시글의 좋아요를 누르고, 삭제한다")
    public String likePost(@PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.likePost(postid, userDetails.getUser());
    }

    @GetMapping("/posts/likes")
    @Operation(summary = "게시글 좋아요순 목록 조회", description = "좋아요 게시글 목록을 조회한다.")
    public List<PostResponse> getPostLike(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostLike(userDetails.getUser());
    }

    @GetMapping("/posts/folllowers")
    @Operation(summary = "게시글 목록 조회 (팔로워)", description = "팔로워 목록을 사용하여 게시글을 조회한다.")
    public List<PostResponse> getPostByFollower(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPostByFollower(userDetails.getUser());
    }
}


package com.sparta.bootlind.controller;

import com.sparta.bootlind.dto.requestDto.CommentRequest;
import com.sparta.bootlind.dto.responseDto.CommentResponse;
import com.sparta.bootlind.dto.responseDto.PostResponse;
import com.sparta.bootlind.security.UserDetailsImpl;
import com.sparta.bootlind.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/comments/param")
    @Operation(summary = "댓글 작성", description = "댓글을 작성한다.")
    public CommentResponse createComment(@RequestParam Long postid, @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  commentService.createComment(postid, request, userDetails.getUser());
    }

    @PutMapping("/comments/{id}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정한다.")
    public CommentResponse updateComment(@PathVariable Long id, @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  commentService.updateComment(id, request, userDetails.getUser());
    }

    @DeleteMapping ("/comments/{id}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제한다.")
    public String deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  commentService.deleteComment(id, userDetails.getUser());
    }

    @PostMapping("/comments/like/{id}")
    @Operation(summary = "댓글 좋아요", description = "댓글의 좋아요를 누르고, 삭제한다")
    public String likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.likeComment(id, userDetails.getUser());
    }

    @GetMapping("/comments/likes")
    @Operation(summary = "게시글 좋아요순 목록 조회", description = "좋아요 게시글 목록을 조회한다.")
    public List<CommentResponse> getCommentLike() {
        return commentService.getCommentLike();
    }

}

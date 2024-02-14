package com.sparta.bootlind.controller;

import com.sparta.bootlind.dto.requestDto.CommentRequest;
import com.sparta.bootlind.dto.responseDto.CommentResponse;
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

    @PostMapping("/comments/{postid}")
    @Operation(summary = "댓글 작성", description = "댓글을 작성한다.")
    public CommentResponse createComment(@PathVariable Long postid, @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postid, request, userDetails.getUser());
    }

    @PutMapping("/comments/{commentid}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정한다.")
    public CommentResponse updateComment(@PathVariable Long commentid, @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentid, request, userDetails.getUser());
    }

    @DeleteMapping("/comments/{commentid}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제한다.")
    public String deleteComment(@PathVariable Long commentid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentid, userDetails.getUser());
    }

    @PostMapping("/comments/likes/{commentid}")
    @Operation(summary = "댓글 좋아요", description = "댓글의 좋아요를 누르고, 삭제한다")
    public String likeComment(@PathVariable Long commentid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.likeComment(commentid, userDetails.getUser());
    }

    @GetMapping("/comments/{postid}")
    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회한다.")
    public List<CommentResponse> getCommentList(@PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.getCommentList(postid, userDetails.getUser());
    }

}

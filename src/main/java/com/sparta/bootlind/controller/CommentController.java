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
    public String deleteComment(@PathVariable Long id, @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  commentService.deleteComment(id, request, userDetails.getUser());
    }




}

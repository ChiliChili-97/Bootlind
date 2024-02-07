package com.sparta.bootlind.service;

import com.sparta.bootlind.dto.requestDto.CommentRequest;
import com.sparta.bootlind.dto.responseDto.CommentResponse;
import com.sparta.bootlind.dto.responseDto.PostResponse;
import com.sparta.bootlind.entity.Comment;
import com.sparta.bootlind.entity.Post;
import com.sparta.bootlind.entity.User;
import com.sparta.bootlind.repository.CommentRepository;
import com.sparta.bootlind.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static PostRepository postRepository;
    private static CommentRepository commentRepository;

    public CommentResponse createComment(Long postid, CommentRequest request, User user) {
        Post post = postRepository.findById(postid).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 게시글이 없습니다.")
        );
        Comment comment = commentRepository.save(new Comment(user,post,request));
        return new CommentResponse(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long id, CommentRequest request, User user) {
        Comment comment = findComment(id);
        if(!comment.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
        comment.update(request);
        return new CommentResponse(comment);
    }



    public String deleteComment(Long id, CommentRequest request, User user) {
        Comment comment = findComment(id);
        if(!comment.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
        commentRepository.delete(comment);
        return id + ": 삭제 완료했습니다";
    }

    public Comment findComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 댓글이 없습니다.")
        );

        return comment;
    }
}

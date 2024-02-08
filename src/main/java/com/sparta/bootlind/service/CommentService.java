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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentResponse createComment(Long id, CommentRequest request, User user) {
        Post post = postRepository.findById(id).orElseThrow(
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



    public String deleteComment(Long id, User user) {
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

    @Transactional
    public String likeComment(Long id, User user) {
        Comment comment = findComment(id);

        if(comment.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("자신의 댓글에는 좋아요를 할 수 없습니다.");

        String like = "/" + user.getId();
        String likes = comment.getLikes();
        if (likes.contains(like)){
            likes = likes.replace(like,"");
            comment.setLikes(likes);
            List<String> list = Arrays.asList(likes.split("/"));
            comment.setLikescnt(list.size()-1);
            return "댓글에 좋아요를 취소합니다." + comment.getLikes()+ " 좋아요 수: " + comment.getLikescnt();
        }
        else{
            likes = like.concat(likes);
            comment.setLikes(likes);
            List<String> list = Arrays.asList(likes.split("/"));
            comment.setLikescnt(list.size()-1);
            return "댓글에 좋아요를 누르셨습니다" + comment.getLikes()+" 좋아요 수: " + comment.getLikescnt();
        }
    }

    public List<CommentResponse> getCommentLike() {
        List<Comment> commentList = commentRepository.findAll(Sort.by(Sort.Direction.DESC, "likescnt"));
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for(Comment comment : commentList){
            commentResponseList.add(new CommentResponse(comment));
        }
        return commentResponseList;
    }
}

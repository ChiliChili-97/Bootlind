package com.sparta.bootlind.service;

import com.sparta.bootlind.dto.requestDto.PostRequest;
import com.sparta.bootlind.dto.responseDto.PostResponse;
import com.sparta.bootlind.entity.Comment;
import com.sparta.bootlind.entity.Post;
import com.sparta.bootlind.entity.User;
import com.sparta.bootlind.repository.CategoryRepository;
import com.sparta.bootlind.repository.CommentRepository;
import com.sparta.bootlind.repository.PostRepository;
import com.sparta.bootlind.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    public PostResponse createPost(PostRequest postRequest, User user) {
        categoryRepository.findByCategory(postRequest.getCategory()).orElseThrow(
                ()-> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.")
        );
        Post post = postRepository.save(new Post(postRequest, user));
        return new PostResponse(post);
    }

    public PostResponse getPostByTitle(String title, User user) {
        Post post = (Post) postRepository.findByTitle(title).orElseThrow(
                ()-> new IllegalArgumentException("해당 title 의 게시글이 없습니다.")
        );

//        if(!post.getUser().getUsername().equals(user.getUsername()))
//            throw new IllegalArgumentException("게시글을 확인할 권한이 없습니다.");

        return new PostResponse(post);
    }

    public List<PostResponse> getPostByCategory(String category, User user) {
        categoryRepository.findByCategory(category).orElseThrow(
                ()-> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다.")
        );
        List<Post> postList = postRepository.findAllByCategory(category);
        List<PostResponse> postResponseList = new ArrayList<>();


//        if(!post.getUser().getUsername().equals(user.getUsername()))
//            throw new IllegalArgumentException("게시글을 확인할 권한이 없습니다.");

        for(Post post : postList){
            postResponseList.add(new PostResponse(post));
        }
        return postResponseList;
    }

    @Transactional
    public PostResponse updatePost(Long id, PostRequest postRequest, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 게시글이 없습니다.")
        );

        if(!post.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");

        post.update(postRequest);
        return new PostResponse(post);
    }



    public List<PostResponse> getPostList(User user) {
        List<Post> postList = postRepository.findAll();
        List<PostResponse> postResponseList = new ArrayList<>();

        for(Post post : postList){
            postResponseList.add(new PostResponse(post));
        }
        return postResponseList;
    }

    public PostResponse getPostById(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 게시글이 없습니다.")
        );
        return new PostResponse(post);
    }

    public String deletePost(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 게시글이 없습니다.")
        );

        if(!post.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("게시글 작성자만 삭제할 수 있습니다.");

        List<Comment> commentList = commentRepository.findAllByPost(post);

        for(Comment comment : commentList)
            commentRepository.deleteById(comment.getId());

        postRepository.deleteById(id);
        return "삭제되었습니다.";


    }

    @Transactional
    public String likePost(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 게시글이 없습니다.")
        );

        if(post.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("자신의 게시물에는 좋아요를 할 수 없습니다.");

        String like = "/" + user.getId();
        String likes = post.getPostLikes();
        if (likes.contains(like)){
            likes = likes.replace(like,"");
            post.setPostLikes(likes);
            List<String> list = Arrays.asList(likes.split("/"));
            post.setLikescnt(list.size()-1);
            return "게시물에 좋아요를 취소합니다." + post.getPostLikes()+ " 좋아요 수: " + post.getLikescnt();
        }
        else{
            likes = like.concat(likes);
            post.setPostLikes(likes);
            List<String> list = Arrays.asList(likes.split("/"));
            post.setLikescnt(list.size()-1);
            return "게시물에 좋아요를 누르셨습니다" + post.getPostLikes()+" 좋아요 수: " + post.getLikescnt();
        }
    }

    public List<PostResponse> getPostLike() {
        List<Post> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "likescnt"));
        List<PostResponse> postResponseList = new ArrayList<>();

        for(Post post : postList){
            postResponseList.add(new PostResponse(post));
        }
        return postResponseList;
    }

    public List<PostResponse> getPostByFollower(User user) {
        String followers[] = user.getFollwers();
        List<PostResponse> postResponseList = new ArrayList<>();
        for(String followerId : followers) {
            User follower = userRepository.findById(Long.parseLong(followerId)).orElseThrow(
                    () -> new IllegalArgumentException("해당 id의 유저가 없습니다.")
            );
            List<Post> postList = postRepository.findAllByUser(follower);
            for (Post post : postList) {
                postResponseList.add(new PostResponse(post));
            }
        }
        return postResponseList;
    }
}

package com.sparta.bootlind.service;

import com.sparta.bootlind.dto.requestDto.PostRequest;
import com.sparta.bootlind.dto.responseDto.PostResponse;
import com.sparta.bootlind.entity.Post;
import com.sparta.bootlind.entity.User;
import com.sparta.bootlind.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    public PostResponse createPost(PostRequest postRequest, User user) {
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
        List<Post> postList = postRepository.findByCategory(category);
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

        postRepository.deleteById(id);
        return "삭제되었습니다.";


    }
}

package com.sparta.bootlind.service;

import com.sparta.bootlind.dto.requestDto.SignupRequest;
import com.sparta.bootlind.entity.Comment;
import com.sparta.bootlind.entity.Post;
import com.sparta.bootlind.entity.User;
import com.sparta.bootlind.entity.UserRoleEnum;
import com.sparta.bootlind.repository.CommentRepository;
import com.sparta.bootlind.repository.PostRepository;
import com.sparta.bootlind.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final String ADMIN_TOKEN = "f679d89c320cc4adb72b7647a64ccbe520406dc3ee4578b44bcffbfa7ebbb85e30b964306b6398d3a2d7098ecd1bc203551e356ac5ec4a5ee0c7dc899fb704c5";

    public void signup(SignupRequest requestDto) {
        String username = requestDto.getUsername();
        String profile = requestDto.getProfile();
        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 검증
        Optional<User> verificationUser = userRepository.findByUsername(username);
        if (verificationUser.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 권한 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 인증키가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, nickname, profile, role);
        userRepository.save(user);
    }

    @Transactional
    public String followById(Long id, User user) {
        User userfollow = userRepository.findByUsername(user.getUsername()).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 사용자가 존재하지 않습니다.")
        );
        userRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 id의 사용자가 존재하지 않습니다.")
        );
        return userfollow.follow(id);
    }

    public String deleteUser(User user) {
        User target = userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다")
        );

        User blank = userRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("알수없음 이 존재하지 않습니다")
        );

        List<Comment> commentList = commentRepository.findAllByUser(target);
        for(Comment comment : commentList)
            comment.updateUser(blank);

        List<Post> postList = postRepository.findAllByUser(target);
        for(Post post : postList)
            post.updateUser(blank);

        userRepository.deleteById(target.getId());
        return target.getUsername() + "삭제되었습니다.";
    }
}

package com.sparta.bootlind.service;

import com.sparta.bootlind.dto.requestDto.*;
import com.sparta.bootlind.dto.responseDto.InfoResponse;
import com.sparta.bootlind.entity.User;
import com.sparta.bootlind.entity.UserRoleEnum;
import com.sparta.bootlind.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "f679d89c320cc4adb72b7647a64ccbe520406dc3ee4578b44bcffbfa7ebbb85e30b964306b6398d3a2d7098ecd1bc203551e356ac5ec4a5ee0c7dc899fb704c5";

    public void signup(@Valid SignupRequest requestDto, BindingResult bindingResult) {
        String username = requestDto.getUsername();
        String profile = requestDto.getProfile();
        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 필드 에러 확인
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if (!fieldErrors.isEmpty()) {

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new IllegalArgumentException("필드 에러가 존재합니다.");

        } else {
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
    }

    @Transactional
    public String followByNickname(String nickname, User user) {
        User userfollow = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 사용자가 존재하지 않습니다.")
        );
        User target = userRepository.findByNickname(nickname).orElseThrow(
                () -> new IllegalArgumentException("해당 nickname의 사용자가 존재하지 않습니다.")
        );


        return userfollow.follow(target.getId());
    }

    @Transactional
    public String deactivateUser(User user) {
        User target = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        if (target.getStatus().equals("DEACTIVATED")) {
            throw new IllegalArgumentException("해당 사용자는 이미 비활성화 상태입니다.");
        }

        if (target.getStatus().equals("DELETED")) {
            throw new IllegalArgumentException("해당 사용자는 탈퇴된 상태입니다. 활성화된 사용자만 비활성화 할 수 있습니다.");
        }

        target.updateStatus("DEACTIVATED");
        return "비활성화 상태로 변경되었습니다.";
    }

    @Transactional
    public String activateUser(User user) {
        User target = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        if (target.getStatus().equals("ACTIVATED")) {
            throw new IllegalArgumentException("해당 사용자는 이미 활성화 상태입니다.");
        }

        if (target.getStatus().equals("DELETED")) {
            throw new IllegalArgumentException("해당 사용자는 탈퇴된 상태입니다. 탈퇴된 사용자는 관리자만 활성화 할 수 있습니다.");
        }

        target.updateStatus("ACTIVATED");
        return "활성화 상태로 변경되었습니다.";
    }

    @Transactional
    public String deleteUser(User user) {
        User target = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        if (target.getStatus().equals("DELETED")) {
            throw new IllegalArgumentException("해당 사용자는 이미 탈퇴 상태입니다.");
        }

        target.updateStatus("DELETED");
        return "탈퇴 상태로 변경되었습니다";
    }

    @Transactional
    public String restoreUser(Long id, SignupRequest requestDto, User user) {

        //if(user!=관리자)
        //    throw  new IllegalArgumentException("관리자만 탈퇴 사용자를 복구할 수 있습니다.");

        User target = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        String username = requestDto.getUsername();
        String profile = requestDto.getProfile();
        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 nickname 입니다.");
        }

        target.updateUser(username, profile, nickname, password);
        target.updateStatus("ACTIVATED");

        return "복구 되었습니다.";
    }

    @Transactional
    public String updateUsername(UpdateUsernameRequest request, User user) {
        User target = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        Optional<User> checkUsername = userRepository.findByUsername(request.getUsername());
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        target.updateUsername(request.getUsername());

        return "수정 되었습니다.";
    }

    @Transactional
    public String updateNickname(UpdateNicknameRequest request, User user) {
        User target = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        Optional<User> checkNickname = userRepository.findByNickname(request.getNickname());
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 nickname 입니다.");
        }

        target.updateNickname(request.getNickname());

        return "수정 되었습니다.";
    }

    @Transactional
    public String updateProfile(UpdateProfileRequest request, User user) {
        User target = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        target.updateProfile(request.getProfile());

        return "수정 되었습니다.";
    }

    @Transactional
    public String updatePassword(UpdatePasswordRequest request, User user) {
        User target = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        if (!passwordEncoder.matches(request.getOldpassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }

        target.updatePassword(passwordEncoder.encode(request.getNewpassword()));

        return "수정 되었습니다.";
    }

    public String accessProfile(Long id, User user, ProfileAccessRequest request) {

        userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        if (!passwordEncoder.matches(request.getInputPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return "비밀번호가 일치합니다. 회원 정보로 이동합니다.";
    }

    public InfoResponse getUserInfo(User user) {
        User target = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );

        String[] follwernumbers = target.getFollwers();
        String[] follwers = new String[target.getFollwers().length];
        for(int i=0; i<follwernumbers.length;i++) {
            follwers[i] = userRepository.findById(Long.parseLong(follwernumbers[i])).orElseThrow(()-> new IllegalArgumentException("없음.")).getNickname();
        }

        return new InfoResponse(target,follwers);
    }
}

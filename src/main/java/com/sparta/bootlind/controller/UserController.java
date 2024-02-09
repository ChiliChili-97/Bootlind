package com.sparta.bootlind.controller;

import com.sparta.bootlind.dto.requestDto.*;
import com.sparta.bootlind.dto.responseDto.SignupResponse;
import com.sparta.bootlind.entity.UserRoleEnum;
import com.sparta.bootlind.security.UserDetailsImpl;
import com.sparta.bootlind.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @Secured(UserRoleEnum.Authority.ADMIN) 어드민 권한 활성화 어노테이션

    // 회원가입 컨트롤러
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 요청을 허가한다.")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest requestDto, BindingResult bindingResult) {
        SignupResponse responseDto = new SignupResponse("회원가입 성공", 201);
        userService.signup(requestDto, bindingResult);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/follows/{userid}")
    @Operation(summary = "팔로우(userid)", description = "다른 사용자를 팔로우/언팔로우 한다.")
    public String followById(@PathVariable Long userid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.followById(userid, userDetails.getUser());
    }

    @PostMapping("/users/{userid}/access")
    @Operation(summary = "프로필 수정 액세스", description = "프로필 수정 페이지에 액세스한다.")
    public String profileAccess(@PathVariable Long userid, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileAccessRequest request) {
        return userService.accessProfile(userid, userDetails.getUser(), request);
    }

    @PutMapping("/users/updates/deactivate")
    @Operation(summary = "회원상태 변경(비활성화)", description = "회원의 상태를 비활성화(DEACTIVATED)로 변경한다.")
    public String deactivateUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deactivateUser(userDetails.getUser());
    }

    @PutMapping("/users/updates/activate")
    @Operation(summary = "회원상태 변경(활성화)", description = "회원의 상태를 활성화(ACTIVATED)로 변경한다.")
    public String activateUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.activateUser(userDetails.getUser());
    }

    @PutMapping("/users/updates/delete")
    @Operation(summary = "회원상태 변경(탈퇴)", description = "회원의 상태를 탈퇴(DELETED)로 변경한다.")
    public String deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deleteUser(userDetails.getUser());
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/users/updates/restore/{userid}")
    @Operation(summary = "회원 복구(관리자메뉴)", description = "탈퇴한 회원의 상태를 활성화(ACTIVATED)로 변경한다.")
    public String restoreUser(@PathVariable Long userid, @RequestBody @Valid SignupRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.restoreUser(userid, requestDto, userDetails.getUser());
    }

    @PutMapping("/users/updates/username")
    @Operation(summary = "회원정보 수정(username)", description = "회원의 username을 변경한다.")
    public String updateUsername(@RequestBody @Valid UpdateUsernameRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUsername(request, userDetails.getUser());
    }

    @PutMapping("/users/updates/nickname")
    @Operation(summary = "회원정보 수정(nickname)", description = "회원의 nickname을 변경한다.")
    public String updateNickname(@RequestBody @Valid UpdateNicknameRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateNickname(request, userDetails.getUser());
    }

    @PutMapping("/users/updates/profile/{profile}")
    @Operation(summary = "회원정보 수정(profile)", description = "회원의 profile을 변경한다.")
    public String updateProfile(@PathVariable String profile, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateProfile(profile, userDetails.getUser());
    }

    @PutMapping("/users/updates/password")
    @Operation(summary = "회원정보 수정(password)", description = "회원의 password를 변경한다.")
    public String updatePassword(@RequestBody @Valid UpdatePasswordRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updatePassword(request, userDetails.getUser());
    }

}

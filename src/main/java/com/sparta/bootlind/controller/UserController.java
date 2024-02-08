package com.sparta.bootlind.controller;

import com.sparta.bootlind.dto.requestDto.UserRequest;
import com.sparta.bootlind.dto.requestDto.SignupRequest;
import com.sparta.bootlind.dto.responseDto.UserResponse;
import com.sparta.bootlind.dto.responseDto.SignupResponse;
import com.sparta.bootlind.security.UserDetailsImpl;
import com.sparta.bootlind.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if (!fieldErrors.isEmpty()) {

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            SignupResponse responseDto = new SignupResponse("회원가입 실패", 403);
            return ResponseEntity.badRequest().body(responseDto);

        } else {
            SignupResponse responseDto = new SignupResponse("회원가입 성공", 201);
            userService.signup(requestDto);
            return ResponseEntity.ok().body(responseDto);
        }
    }

    @PutMapping("/users/update")
    @Operation(summary = "프로필 수정", description = "프로필 수정 요청을 허가한다.")
    public UserResponse updateProfile(@RequestBody UserRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.updateProfile(requestDto, userDetails.getUser());
    }

    @DeleteMapping("/users/delete")
    @Operation(summary = "계정 삭제", description = "계정을 삭제하며 게시글의 소유를 '알수없음' 으로 전환한다.")
    public String deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userService.delete(userDetails.getUser());
    }

    @PutMapping("/users/update")
    @Operation(summary = "프로필 수정", description = "프로필 수정 요청을 허가한다.")
    public UserResponse updateProfile(@RequestBody UserRequest requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.updateProfile(requestDto, userDetails.getUser());
    }
}

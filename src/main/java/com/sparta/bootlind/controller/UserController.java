package com.sparta.bootlind.controller;

import com.sparta.bootlind.dto.requestDto.SignupRequest;
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

    @PostMapping("/follow/{id}")
    @Operation(summary = "팔로우(id)", description = "다른 사용자를 팔로우/언팔로우 한다.")
    public String followById(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.followById(id, userDetails.getUser());
    }

    @DeleteMapping("user/delete")
    @Operation(summary = "회원탈퇴", description = "회원 탈퇴를 하며 기존 게시글의 소유를 넘긴다.")
    public String deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.deleteUser(userDetails.getUser());
    }
}

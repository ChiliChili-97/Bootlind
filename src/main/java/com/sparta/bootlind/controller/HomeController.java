package com.sparta.bootlind.controller;

import com.sparta.bootlind.dto.requestDto.SignupRequest;
import com.sparta.bootlind.dto.responseDto.UserInfoResponse;
import com.sparta.bootlind.entity.UserRoleEnum;
import com.sparta.bootlind.security.UserDetailsImpl;
import com.sparta.bootlind.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/signup")
    public String signup(@Valid SignupRequest requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/signup";
        }

        userService.signup(requestDto, bindingResult);

        return "redirect:/login";
    }

    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoResponse getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);

        return new UserInfoResponse(username, isAdmin);
    }
}

package com.sparta.bootlind.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d].{4,10}$",
            message = "아이디는 최소 4자 이상, 10자 이하로 알파벳 소문자와 숫자로 구성되어야 합니다.")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*.])(?=.*\\d)[a-zA-Z!@#$%^&*.\\d].{8,15}$",
            message = "비밀번호는 최소 7자 이상, 15자 이하로 알파벳과 특수문자, 숫자로 구성되어야 합니다.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\d]{2,10}$",
            message = "닉네임은 2~10자로 구성되어야 합니다.")
    private String nickname;

    private String profile;
    private boolean admin = false;
    private String adminToken = "";

}

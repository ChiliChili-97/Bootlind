package com.sparta.bootlind.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProfileAccessRequest {

    @NotBlank
    private String inputPassword;
}

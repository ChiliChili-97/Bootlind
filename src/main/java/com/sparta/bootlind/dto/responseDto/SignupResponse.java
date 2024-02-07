package com.sparta.bootlind.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SignupResponse {
    private String msg;
    private int statusCode;
}

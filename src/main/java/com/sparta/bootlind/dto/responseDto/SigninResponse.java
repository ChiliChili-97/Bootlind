package com.sparta.bootlind.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SigninResponse {
    private String msg;
    private int statusCode;
}

package com.sparta.bootlind.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    String username;
    boolean isAdmin;
}

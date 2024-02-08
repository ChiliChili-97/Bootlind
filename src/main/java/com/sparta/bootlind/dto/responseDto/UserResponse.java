package com.sparta.bootlind.dto.responseDto;

import com.sparta.bootlind.entity.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    private Long id;
    private String username;
    private String nickname;
    private String profile;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.profile = user.getProfile();
    }
}
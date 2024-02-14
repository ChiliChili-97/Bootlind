package com.sparta.bootlind.dto.responseDto;

import com.sparta.bootlind.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoResponse {
    private String username;
    private String nickname;
    private String profile;
    private String status;
    private String role;
    private String[] followers;

    public InfoResponse(User target, String[] followers) {
        this.username = target.getUsername();
        this.nickname = target.getNickname();
        this.profile = target.getProfile();
        this.status = target.getStatus();
        this.role = target.getRole().toString();
        this.followers = followers;
    }
}

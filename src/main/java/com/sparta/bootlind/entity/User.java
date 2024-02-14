package com.sparta.bootlind.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = true)
    private String profile;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = true)
    private String follow;

    @Column(nullable = false)
    private String status;

    public User(String username, String password, String nickname, String profile, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
        this.role = role;
        this.follow = "follwers :";
        this.status = "ACTIVATED";
    }

    public String[] getFollwers() {
        String follwer[] = this.follow.split("/");
        String follwerList = Arrays.toString(follwer);
        follwerList = follwerList.replace("follwers :, ", "");
        follwerList = follwerList.replaceAll(" ", "");
        follwerList = follwerList.replace("[", "");
        follwerList = follwerList.replace("]", "");
        String follwers[] = follwerList.split(",");
        return follwers;
    }

    public String follow(Long Id) {
        if (this.getId() == Id) {
            return "자신을 팔로우 할 수 없습니다.";
        }
        String follower = this.follow;
        String userId = "/" + Id;
        if (follower.contains(userId)) {
            this.follow = this.follow.replace(userId, "");
            return "언팔로우 되었습니다.";
        } else {
            this.follow = this.follow.concat(userId);
            return "팔로우 되었습니다.";
        }
    }


    public void updateStatus(String status) {
        switch (status) {
            case "ACTIVATED":
                this.status = "ACTIVATED";
                break;

            case "DEACTIVATED":
                this.status = "DEACTIVATED";
                break;

            case "DELETED":
                this.username = "DELETED_" + this.username;
                this.password = "DELETED_" + this.password;
                this.nickname = "DELETED_" + this.nickname;
                this.profile = "DELETED_" + this.profile;
                this.status = "DELETED";
                break;
        }
    }


    public void updateUser(String username, String profile, String nickname, String password) {
        this.username = username;
        this.profile = profile;
        this.nickname = nickname;
        this.password = password;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfile(String profile) {
        this.profile = profile;
    }

    public void updatePassword(String encode) {
        this.password = encode;
    }
}

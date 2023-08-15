package com.example.practiceex.user.dto;

import com.example.practiceex.user.entity.User;
import com.example.practiceex.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String email;
    private boolean admin;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.admin = user.getRole().equals(UserRoleEnum.ADMIN);
    }
}

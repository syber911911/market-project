package com.likelion.market.dto;

import lombok.Data;

@Data
public class UserDto {
    private String writer;
    private String password;

    @Data
    public static class UpdateUserRequest {
        private UserDto recentUser;
        private UserDto updateUser;
    }
}

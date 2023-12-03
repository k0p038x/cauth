package com.wtf.cauth.data.dto.response.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class UserDto {
    private String id;
    private String name;
    private String email;
    private Boolean emailVerified;
    private boolean passwordConfigured;
    private String role;
    private String appName;
}

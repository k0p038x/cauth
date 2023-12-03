package com.wtf.cauth.data.dto.request.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class UserAddReqDto {
    private String id;
    private String name;
    private String email;
    private Boolean emailVerified;
    private String password;
    private String appName;
    private String role;
}

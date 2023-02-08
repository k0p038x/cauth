package com.wtf.cauth.data.dto.request.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class UserPasswordUpdateReqDto {
    private String id;
    private String curPassword;
    private String newPassword;
    private boolean reset;
    private String appName;
}

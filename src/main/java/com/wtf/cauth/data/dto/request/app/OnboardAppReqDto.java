package com.wtf.cauth.data.dto.request.app;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class OnboardAppReqDto {
    private String name;
    private String ownerEmail;
    private int userAuthTokenExpiryInMins;
}

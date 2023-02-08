package com.wtf.cauth.data.dto.response.app;


import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class AppDto {
    private String id;
    private String name;
    private String ownerEmail;
    private long userAuthTokenExpiryInMins;
}

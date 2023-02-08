package com.wtf.cauth.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "apps")
public class App {
    @Id
    private String id;
    private String name;
    private String ownerEmail;
    private String secret;
    private int userAuthTokenExpiryInMins;
}

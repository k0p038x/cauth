package com.wtf.cauth.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_credentials")
public class UserCredential {
    @Id
    private String id;
    private String userId;
    private String password;
    private long createdOn;
    private boolean active;

    public static UserCredential newCredential(String userId, String password) {
        return new UserCredential(UUID.randomUUID().toString(), userId, password,
            System.currentTimeMillis(), true);
    }
}

package com.wtf.cauth.repository;

import com.wtf.cauth.data.model.UserCredential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserCredentialRepository extends CrudRepository<UserCredential, String> {
    List<UserCredential> findUserCredentialByUserIdAndActive(String userId, boolean active);
    List<UserCredential> findUserCredentialByUserId(String userId);
    List<UserCredential> findUserCredentialByUserIdInAndActive(Collection<String> userIds, boolean active);
}

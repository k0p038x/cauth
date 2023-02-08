package com.wtf.cauth.repository;

import com.wtf.cauth.data.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findById(String id);
    List<User> findByAppId(String appId);
}

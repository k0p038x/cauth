package com.wtf.cauth.repository;

import com.wtf.cauth.data.model.App;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppRepository extends CrudRepository<App, String> {
    Optional<App> findByName(String name);
    Optional<App> findById(String id);
    List<App> findAll();
}

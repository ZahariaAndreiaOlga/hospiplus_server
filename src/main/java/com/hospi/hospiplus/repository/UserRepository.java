package com.hospi.hospiplus.repository;

import com.hospi.hospiplus.model.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT * FROM `User` WHERE email = :email")
    Optional<User> findUserByEmail(String email);
}

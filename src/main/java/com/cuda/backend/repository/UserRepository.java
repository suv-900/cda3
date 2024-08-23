package com.cuda.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cuda.backend.entities.User;

public interface UserRepository extends JpaRepository{
    void add(User user)throws Exception;
    
    Optional<User> getByName(String username)throws Exception;
    
    User update(User user)throws Exception;
    
    void delete(User user)throws Exception;
}

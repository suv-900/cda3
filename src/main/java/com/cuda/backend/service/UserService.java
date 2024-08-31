package com.cuda.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cuda.backend.entities.User;

@Service
public interface UserService {

    Long register(String name,String password,String email);

    void login(String name,String password);

    String generateToken(String name);

    void validateToken(String token);

    void delete(Long id);

    Optional<User> getById(Long userID);  
    
    void follow(Long following,Long follower);

    List<User> getFollowers(Long id);
    
    List<User> getFollowers(Long id,int pageCount);

    List<User> getFollowing(Long id,int pageCount);

    long count();

    Long countOfActiveUsers();

    List<User> listOfActiveUsers(int pageCount);


}

package com.cuda.backend.services;

import java.util.List;
import java.util.Optional;

import com.cuda.backend.entities.User;

public interface UserService {

    boolean existsByName(String name);

    Long register(User user);

    void login(String name,String password);
    
    User update(User user);

//    String generateToken(String name);

//    void validateToken(String token);

    // void updateProfilePicture(Long userId,Image image);

    // void removeProfilePicture(Long userId);

    void delete(Long id);

    Optional<User> getById(Long userID);  
    
//    void follow(Long following,Long follower);

    List<User> getFollowers(Long id,int pageCount);

    List<User> getFollowing(Long id,int pageCount);

    void follow(Long followerId,Long followingId);

    void unfollow(Long followerId,Long followingId);

    long count();

//    Long countOfActiveUsers();

    List<User> listOfActiveUsers(int pageCount);


}

package com.cuda.backend.services;

import java.util.List;

import com.cuda.backend.entities.User;
import com.cuda.backend.entities.dto.UserCreationDTO;
import com.cuda.backend.entities.dto.UserDTO;

public interface UserService {
    UserDTO getById(Long userId);

    UserDTO getByName(String username);

    boolean existsByName(String name);

    void register(UserCreationDTO userDto);

    void login(String name,String password);
    
    User update(User user);

    void deleteById(Long id);

    void delete(User user);

    void deleteAll();

    List<UserDTO> getFollowers(Long id,int pageCount);

    List<UserDTO> getFollowing(Long id,int pageCount);

    void follow(Long followerId,Long followingId);

    void unfollow(Long followerId,Long followingId);

    long count();

    List<User> getAll();

    List<User> listOfActiveUsers(int pageCount);
}

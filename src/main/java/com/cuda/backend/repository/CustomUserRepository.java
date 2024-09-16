package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import com.cuda.backend.entities.User;
import com.cuda.backend.entities.dto.UserDTO;

public interface CustomUserRepository {
    Optional<User> existsByName(String username)throws Exception;

    List<UserDTO> getFollowers(Long id,int pageNumber,int pageSize);

    List<UserDTO> getFollowing(Long id,int pageNumber,int pageSize);

    void addFollower(Long ownerID,Long followerID);

    void removeFollower(Long ownerID,Long followerID);
}

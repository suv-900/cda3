package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import com.cuda.backend.entities.dto.UserDTO;

public interface CustomUserRepository {
    Optional<UserDTO> getByIdDTO(Long userId);

    Optional<UserDTO> getByNameDTO(String username);

    boolean existsByName(String username);

    List<UserDTO> getFollowers(Long id,int pageNumber,int pageSize);

    List<UserDTO> getFollowing(Long id,int pageNumber,int pageSize);

    void addFollower(Long ownerID,Long followerID);

    void removeFollower(Long ownerID,Long followerID);

    void increaseFollowerCount(Long userId);
    
    void increaseFollowingCount(Long userId);
    
    void increaseTweetCount(Long userId);

    void decreaseFollowerCount(Long userId);
    
    void decreaseFollowingCount(Long userId);
    
    void decreaseTweetCount(Long userId);

}

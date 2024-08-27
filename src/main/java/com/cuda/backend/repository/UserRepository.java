package com.cuda.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cuda.backend.entities.User;

/**
 * this has its own dao and extends jparepository
 */
public interface UserRepository extends JpaRepository<User,Long>{
    
    Optional<User> existsByName(String username)throws Exception;

    List<User> getFollowers(Long userID);

    List<User> getFollowing(Long userID);

    void addFollower(Long ownerID,Long followerID);

    void removeFollower(Long ownerID,Long followerID);

}

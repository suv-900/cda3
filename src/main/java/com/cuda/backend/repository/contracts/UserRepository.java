package com.cuda.backend.repository.contracts;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cuda.backend.entities.User;

public interface UserRepository extends JpaRepository<User,Long>{
    void add(User user)throws Exception;
    
    Optional<User> getByName(String username)throws Exception;
    
    boolean exists(String username)throws Exception;
    
    Optional<User> getByID(Long userID)throws Exception;

    List<User> getByIds(List<Long> userIds)throws Exception;

    List<User> getMostFollowedUsers(Pageable page)throws Exception;

    List<User> getRelatedUsers()throws Exception;

    User update(User user)throws Exception;
    
    void delete(User user);
}

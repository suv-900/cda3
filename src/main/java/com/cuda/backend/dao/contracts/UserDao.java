package com.cuda.backend.dao.contracts;

import java.util.Optional;
import java.util.Set;

import com.cuda.backend.entities.User;

public interface UserDao {
   void add(User user)throws Exception;

   User getById(long userID)throws Exception;

   Optional<User> getByIdOptional(long userID)throws Exception;
   
   Optional<User> getByName(String username)throws Exception;

   User update(User user)throws Exception;
   
   void delete(User user)throws Exception;
   
   String getUserPassword(String username)throws Exception;

   Set<User> getUserFollowers(Long userID)throws Exception;

   Set<User> getUserFollowing(Long userID)throws Exception;

   void addFollower(User owner,User follower)throws Exception;
   
   void removeFollower(User owner,User follower)throws Exception;
}

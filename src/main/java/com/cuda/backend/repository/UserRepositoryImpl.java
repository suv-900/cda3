package com.cuda.backend.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.cuda.backend.dao.TweetDaoImpl;
import com.cuda.backend.dao.UserDaoImpl;
import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;
import com.cuda.backend.exceptions.RecordNotFoundException;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

/**
 * this talks with dao to interact with db.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private UserDaoImpl userDao;
    
    private TweetDaoImpl tweetDao;

    public void add(User user)throws Exception{
        userDao.add(user);
    }

    public Optional<User> getByName(String username)throws Exception{
        return userDao.getByName(username);
    }

    public Optional<User> getById(Long id) throws Exception{
        return userDao.getByID(id);
    }
    public User update(User user)throws Exception{
        return userDao.update(user);
    }

    public void delete(User user)throws Exception{
        userDao.delete(user);
    }

    public String getUserPassword(String username)throws RecordNotFoundException,IllegalStateException{
        String dbpassword = null;
        try{
            dbpassword = userDao.getUserPassword(username);
        }catch(NonUniqueResultException err){
            throw new IllegalStateException(err.getMessage());
        }catch(NoResultException err){
            throw new RecordNotFoundException(err.getMessage());
        }
        return dbpassword;
    }
    
    public boolean exists(String username){
        User user = new User();
        user.setName(username);
        return userDao.exists(user);
    }

    public Set<Tweet> getUserTweets(Long id)throws Exception{

    }

    public Set<Tweet> getUserTweetsSortByOldest(Long id)throws Exception{

    }

    public Set<Tweet> getUserTweetsSortbyNewest(Long id)throws Exception{

    }

    public Set<Tweet> getUserTweetsSortByBest(Long id)throws Exception{

    }
}

package com.cuda.backend.repository.beans;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.cuda.backend.dao.beans.TweetDaoBean;
import com.cuda.backend.dao.beans.UserDaoBean;
import com.cuda.backend.dao.contracts.TweetDao;
import com.cuda.backend.dao.contracts.UserDao;
import com.cuda.backend.entities.Tweet;
import com.cuda.backend.entities.User;
import com.cuda.backend.exceptions.RecordNotFoundException;
import com.cuda.backend.repository.contracts.UserRepository;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

/**
 * this talks with dao to interact with db.
 */
@Repository
public class UserRepositoryBean implements UserRepository {
    
    private UserDao userDao;
    private TweetDao tweetDao;

    public UserRepositoryBean(UserDao userDao,TweetDao tweetDao){
        this.userDao = userDao;
        this.tweetDao = tweetDao;
    }

    public void add(User user)throws Exception{
        userDao.add(user);
    }

    public Optional<User> getByName(String username)throws Exception{
        return userDao.getByName(username);
    }

    public User getById(Long id){
        return userDao.getById(id);
    }

    public Optional<User> getByIdOptional(Long id) throws Exception{
        return userDao.getByIdOptional(id);
    }
    public User update(User user)throws Exception{
        return userDao.update(user);
    }

    public void delete(User user){
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

    public List<User> getMostFollowedUsers(Pageable page){

    }

}
